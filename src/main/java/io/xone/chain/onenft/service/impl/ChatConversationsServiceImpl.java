package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import io.xone.chain.onenft.entity.ChatConversations;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.ChatConversationsMapper;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.PageRequest;
import io.xone.chain.onenft.resp.ChatConversationResp;
import io.xone.chain.onenft.service.IChatConversationsService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等） 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@Service
@RequiredArgsConstructor
public class ChatConversationsServiceImpl extends ServiceImpl<ChatConversationsMapper, ChatConversations> implements IChatConversationsService {

    private final UsersMapper usersMapper;

    @Override
    public Page<ChatConversationResp> getUserConversations(PageRequest request, String walletAddress) {
        Page<ChatConversations> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<ChatConversations> query = new LambdaQueryWrapper<>();
        query.and(w -> w.eq(ChatConversations::getWalletAddressA, walletAddress)
                        .or()
                        .eq(ChatConversations::getWalletAddressB, walletAddress));
        
        query.orderByDesc(ChatConversations::getLastMsgTime); // Sort by latest message
        
        Page<ChatConversations> pResult = page(page, query);
        Page<ChatConversationResp> respPage = new Page<>(pResult.getCurrent(), pResult.getSize(), pResult.getTotal());
        
        if (pResult.getRecords().isEmpty()) {
            return respPage;
        }
        
        // Collect other user addresses
        List<String> otherAddresses = new ArrayList<>();
        for (ChatConversations c : pResult.getRecords()) {
            if (walletAddress.equals(c.getWalletAddressA())) {
                otherAddresses.add(c.getWalletAddressB());
            } else {
                otherAddresses.add(c.getWalletAddressA());
            }
        }
        
        Map<String, Users> userMap = java.util.Collections.emptyMap();
        if (!otherAddresses.isEmpty()) {
            LambdaQueryWrapper<Users> userQuery = new LambdaQueryWrapper<>();
            userQuery.in(Users::getWalletAddress, otherAddresses);
            List<Users> users = usersMapper.selectList(userQuery);
            userMap = users.stream().collect(Collectors.toMap(Users::getWalletAddress, u -> u));
        }
        
        List<ChatConversationResp> records = new ArrayList<>();
        for (ChatConversations c : pResult.getRecords()) {
            ChatConversationResp resp = new ChatConversationResp();
            BeanUtils.copyProperties(c, resp);
            
            String otherAddr = walletAddress.equals(c.getWalletAddressA()) ? c.getWalletAddressB() : c.getWalletAddressA();
            resp.setOtherAddress(otherAddr);
            
            // Unread count: If I am A, read unreadA. If I am B, read unreadB.
            // Wait, logic check: unreadA means "messages for A that are unread". YES.
            if (walletAddress.equals(c.getWalletAddressA())) {
                resp.setUnreadCount(c.getUnreadA());
            } else {
                resp.setUnreadCount(c.getUnreadB());
            }
            
            if (userMap.containsKey(otherAddr)) {
                Users u = userMap.get(otherAddr);
                resp.setOtherName(u.getName());
                resp.setOtherAvatar(u.getAvatarUrl());
                resp.setOtherIsOnline(StpUtil.isLogin(u.getId()));// Check if user is online
            }
            records.add(resp);
        }
        respPage.setRecords(records);
        return respPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatConversations getOrCreateConversation(String addressA, String addressB) {
        // Ensure consistent ordering to prevent duplicates (A-B vs B-A)
        // Usually we sort them. But let's check if we query both ways or enforce order.
        // Let's enforce alphabetical order or just query using OR.
        // Assuming we look for (A,B) or (B,A).
        
        LambdaQueryWrapper<ChatConversations> query = new LambdaQueryWrapper<>();
        query.and(w -> w.eq(ChatConversations::getWalletAddressA, addressA).eq(ChatConversations::getWalletAddressB, addressB))
             .or(w -> w.eq(ChatConversations::getWalletAddressA, addressB).eq(ChatConversations::getWalletAddressB, addressA));
        
        ChatConversations existing = getOne(query);
        if (existing != null) {
            return existing;
        }
        
        ChatConversations newConv = new ChatConversations();
        // Store in specific order? Maybe lexicographical?
        if (addressA.compareTo(addressB) < 0) {
            newConv.setWalletAddressA(addressA);
            newConv.setWalletAddressB(addressB);
        } else {
            newConv.setWalletAddressA(addressB);
            newConv.setWalletAddressB(addressA);
        }
        newConv.setCreatedAt(LocalDateTime.now());
        newConv.setUpdatedAt(LocalDateTime.now());
        newConv.setUnreadA(0);
        newConv.setUnreadB(0);
        save(newConv);
        return newConv;
    }

    @Override
    public void updateLastMessage(Long conversationId, Long lastMsgId, String preview, LocalDateTime time) {
        LambdaUpdateWrapper<ChatConversations> update = new LambdaUpdateWrapper<>();
        update.eq(ChatConversations::getId, conversationId)
              .set(ChatConversations::getLastMsgId, lastMsgId)
              .set(ChatConversations::getLastMsgPreview, preview)
              .set(ChatConversations::getLastMsgTime, time)
              .set(ChatConversations::getUpdatedAt, LocalDateTime.now());
        update(update);
    }

    @Override
    public void incrementUnread(Long conversationId, String recipientAddress) {
        // Find conversation to know if recipient is A or B
        ChatConversations c = getById(conversationId);
        if (c == null) return;
        
        LambdaUpdateWrapper<ChatConversations> update = new LambdaUpdateWrapper<>();
        update.eq(ChatConversations::getId, conversationId);
        
        if (recipientAddress.equals(c.getWalletAddressA())) {
            update.setSql("unread_a = unread_a + 1");
        } else if (recipientAddress.equals(c.getWalletAddressB())) {
            update.setSql("unread_b = unread_b + 1");
        }
        update(update);
    }

    @Override
    public void markAsRead(Long conversationId, String walletAddress) {
        ChatConversations c = getById(conversationId);
        if (c == null) return;
        
        LambdaUpdateWrapper<ChatConversations> update = new LambdaUpdateWrapper<>();
        update.eq(ChatConversations::getId, conversationId);
        
        if (walletAddress.equals(c.getWalletAddressA())) {
            update.set(ChatConversations::getUnreadA, 0);
        } else if (walletAddress.equals(c.getWalletAddressB())) {
            update.set(ChatConversations::getUnreadB, 0);
        }
        update(update);
    }
}