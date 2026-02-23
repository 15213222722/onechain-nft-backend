package io.xone.chain.onenft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.request.NotificationQueryRequest;
import io.xone.chain.onenft.resp.NotificationResp;
import io.xone.chain.onenft.service.IUserNotificationsService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 系统通知表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final IUserNotificationsService userNotificationsService;
    private final IUsersService usersService;

    @PostMapping("/list")
    public Result<Page<NotificationResp>> list(@RequestBody NotificationQueryRequest request) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        Page<NotificationResp> page = new Page<>(request.getCurrent(), request.getSize());
        return Result.success(userNotificationsService.getUserNotifications(page, currentWalletAddress, request.getIsRead()));
    }

    @PostMapping("/unread")
    public Result<Long> getUnreadCount() {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userNotificationsService.getUnreadCount(currentWalletAddress));
    }

    @PostMapping("/read/{id}")
    public Result<Boolean> markAsRead(@PathVariable Integer id) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userNotificationsService.markAsRead(currentWalletAddress, id));
    }

    @PostMapping("/read/all")
    public Result<Boolean> markAllAsRead() {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userNotificationsService.markAllAsRead(currentWalletAddress));
    }
}