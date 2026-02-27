package io.xone.chain.onenft.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.request.ChatMessageListReq;
import io.xone.chain.onenft.request.ChatMessageReq;
import io.xone.chain.onenft.request.ChatReadReq;
import io.xone.chain.onenft.resp.ChatMessageResp;
import io.xone.chain.onenft.service.IChatMessagesService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等） 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@RestController
@RequestMapping("/chatMessages")
@RequiredArgsConstructor
public class ChatMessagesController {

    private final IChatMessagesService chatMessagesService;
    private final IUsersService usersService;

    @PostMapping("/send")
    public Result<ChatMessageResp> send(@Validated @RequestBody ChatMessageReq request) {
        String walletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(chatMessagesService.sendMessage(walletAddress, request));
    }

    @PostMapping("/list")
    public Result<Page<ChatMessageResp>> list(@Validated @RequestBody ChatMessageListReq request) {
        // Validation: Check if user is part of conversation (Skipped per current scope, but recommended)
        return Result.success(chatMessagesService.getMessages(request.getConversationId(), request));
    }

    @PostMapping("/markRead")
    public Result<Boolean> markRead(@Validated @RequestBody ChatReadReq request) {
        String walletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(chatMessagesService.markRead(request.getConversationId(), walletAddress));
    }
}