package io.xone.chain.onenft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.request.PageRequest;
import io.xone.chain.onenft.resp.ChatConversationResp;
import io.xone.chain.onenft.service.IChatConversationsService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等） 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@RestController
@RequestMapping("/chatConversations")
@RequiredArgsConstructor
public class ChatConversationsController {

    private final IChatConversationsService chatConversationsService;
    private final IUsersService usersService;

    @PostMapping("/list")
    public Result<Page<ChatConversationResp>> list(@RequestBody PageRequest request) {
        String walletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(chatConversationsService.getUserConversations(request, walletAddress));
    }
}