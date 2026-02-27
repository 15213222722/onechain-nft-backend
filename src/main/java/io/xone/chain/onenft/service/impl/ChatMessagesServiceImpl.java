package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.ChatConversations;
import io.xone.chain.onenft.entity.ChatMessages;
import io.xone.chain.onenft.mapper.ChatMessagesMapper;
import io.xone.chain.onenft.request.ChatMessageReq;
import io.xone.chain.onenft.request.PageRequest;
import io.xone.chain.onenft.resp.ChatMessageResp;
import io.xone.chain.onenft.service.IChatConversationsService;
import io.xone.chain.onenft.service.IChatMessagesService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等） 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessagesServiceImpl extends ServiceImpl<ChatMessagesMapper, ChatMessages> implements IChatMessagesService {

    private final IChatConversationsService chatConversationsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessageResp sendMessage(String fromAddress, ChatMessageReq req) {
        // 1. Get or Create Conversation
    	log.info("Sending message from {} to {}, content: {}, type: {}", fromAddress, req.getToAddress(), req.getContent(), req.getMsgType());
        ChatConversations conversation = chatConversationsService.getOrCreateConversation(fromAddress, req.getToAddress());
        
        // 2. Create Message
        ChatMessages msg = new ChatMessages();
        msg.setChatConversationsId(conversation.getId());
        msg.setFromAddress(fromAddress);
        msg.setToAddress(req.getToAddress());
        msg.setMsgType(req.getMsgType());
        msg.setContent(req.getContent());
        if(StrUtil.isNotBlank(req.getExtra())) {
        	msg.setExtra(req.getExtra());
        }
        msg.setIsRead(false);
        msg.setDeletedByFrom(false);
        msg.setDeletedByTo(false);
        msg.setCreatedAt(LocalDateTime.now());
        
        save(msg);
        
        // 3. Update Conversation (last msg, unread count)
        String preview = req.getContent();
        if (preview.length() > 50) {
            preview = preview.substring(0, 50) + "...";
        }
        // If message is not text (e.g. Image), set preview text accordingly
        // For simplicity, just using content. In real app, check msgType.
        if (req.getMsgType() == 2) {
            preview = "[图片]";
        } else if (req.getMsgType() == 4) {
             preview = "[NFT]";
        }
        
        chatConversationsService.updateLastMessage(conversation.getId(), msg.getId(), preview, msg.getCreatedAt());
        chatConversationsService.incrementUnread(conversation.getId(), req.getToAddress());
        
        ChatMessageResp resp = new ChatMessageResp();
        BeanUtils.copyProperties(msg, resp);
        return resp;
    }

    @Override
    public Page<ChatMessageResp> getMessages(Long conversationId, PageRequest request) {
        Page<ChatMessages> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<ChatMessages> query = new LambdaQueryWrapper<>();
        query.eq(ChatMessages::getChatConversationsId, conversationId);
        query.orderByDesc(ChatMessages::getCreatedAt);
        
        Page<ChatMessages> pResult = page(page, query);
        Page<ChatMessageResp> respPage = new Page<>(pResult.getCurrent(), pResult.getSize(), pResult.getTotal());
        
        if (pResult.getRecords().isEmpty()) {
            return respPage;
        }
        
        java.util.List<ChatMessageResp> records = new java.util.ArrayList<>();
        for (ChatMessages m : pResult.getRecords()) {
            ChatMessageResp r = new ChatMessageResp();
            BeanUtils.copyProperties(m, r);
            records.add(r);
        }
        respPage.setRecords(records);
        return respPage;
    }

    @Override
    public boolean markRead(Long conversationId, String readerAddress) {
        // Update messages
        LambdaUpdateWrapper<ChatMessages> update = new LambdaUpdateWrapper<>();
        update.eq(ChatMessages::getChatConversationsId, conversationId)
              .eq(ChatMessages::getToAddress, readerAddress)
              .eq(ChatMessages::getIsRead, false)
              .set(ChatMessages::getIsRead, true)
              .set(ChatMessages::getReadAt, LocalDateTime.now());
        
        update(update);
        
        // Update conversation unread count
        chatConversationsService.markAsRead(conversationId, readerAddress);
        return true;
    }
}