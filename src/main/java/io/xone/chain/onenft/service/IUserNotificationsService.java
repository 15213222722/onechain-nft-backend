package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.UserNotifications;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户通知关系表：记录通知接收人与已读状态 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-23
 */
public interface IUserNotificationsService extends IService<UserNotifications> {

    /**
     * Get user notifications
     * @param page
     * @param walletAddress
     * @param isRead (optional)
     * @return
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.xone.chain.onenft.resp.NotificationResp> getUserNotifications(
           com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.xone.chain.onenft.resp.NotificationResp> page,
           String walletAddress,
           Boolean isRead);

    /**
     * Mark notification as read
     * @param walletAddress
     * @param notificationId
     * @return
     */
    boolean markAsRead(String walletAddress, Integer notificationId);

    /**
     * Mark all notifications as read
     * @param walletAddress
     * @return
     */
    boolean markAllAsRead(String walletAddress);

    /**
     * Get unread count
     * @param walletAddress
     * @return
     */
    long getUnreadCount(String walletAddress);
}