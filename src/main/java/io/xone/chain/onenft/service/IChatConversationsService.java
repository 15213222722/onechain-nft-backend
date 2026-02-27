package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import io.xone.chain.onenft.entity.ChatConversations;
import io.xone.chain.onenft.request.PageRequest;
import io.xone.chain.onenft.resp.ChatConversationResp;

/**
 * <p>
 * 1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等） 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
public interface IChatConversationsService extends IService<ChatConversations> {

    /**
     * Get user conversations list
     * @param request
     * @param walletAddress
     * @return
     */
    Page<ChatConversationResp> getUserConversations(PageRequest request, String walletAddress);

    /**
     * Get or create conversation between two users
     * @param addressA
     * @param addressB
     * @return
     */
    ChatConversations getOrCreateConversation(String addressA, String addressB);

    /**
     * Update last message info
     * @param conversationId
     * @param lastMsgId
     * @param preview
     * @param time
     */
    void updateLastMessage(Long conversationId, Long lastMsgId, String preview, java.time.LocalDateTime time);

    /**
     * Increment unread count for recipient
     * @param conversationId
     * @param recipientAddress
     */
    void incrementUnread(Long conversationId, String recipientAddress);

    /**
     * Mark conversation as read (clear unread count)
     * @param conversationId
     * @param walletAddress
     */
    void markAsRead(Long conversationId, String walletAddress);

}