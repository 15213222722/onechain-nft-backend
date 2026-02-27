package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import io.xone.chain.onenft.entity.ChatMessages;
import io.xone.chain.onenft.request.ChatMessageReq;
import io.xone.chain.onenft.request.PageRequest;
import io.xone.chain.onenft.resp.ChatMessageResp;

/**
 * <p>
 * 1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等） 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
public interface IChatMessagesService extends IService<ChatMessages> {

    /**
     * Send message
     * @param fromAddress
     * @param req
     * @return
     */
    ChatMessageResp sendMessage(String fromAddress, ChatMessageReq req);

    /**
     * Get messages for conversation
     * @param conversationId
     * @param request
     * @return
     */
    Page<ChatMessageResp> getMessages(Long conversationId, PageRequest request);

    /**
     * Mark messages read
     * @param conversationId
     * @param readerAddress
     * @return
     */
    boolean markRead(Long conversationId, String readerAddress);
}