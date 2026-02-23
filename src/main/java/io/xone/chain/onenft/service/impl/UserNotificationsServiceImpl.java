package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.UserNotifications;
import io.xone.chain.onenft.mapper.UserNotificationsMapper;
import io.xone.chain.onenft.resp.NotificationResp;
import io.xone.chain.onenft.service.IUserNotificationsService;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户通知关系表：记录通知接收人与已读状态 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-23
 */
@Service
public class UserNotificationsServiceImpl extends ServiceImpl<UserNotificationsMapper, UserNotifications> implements IUserNotificationsService {

    @Override
    public Page<NotificationResp> getUserNotifications(Page<NotificationResp> page, String walletAddress, Boolean isRead) {
        return baseMapper.getUserNotifications(page, walletAddress, isRead);
    }

    @Override
    public boolean markAsRead(String walletAddress, Integer notificationId) {
        LambdaUpdateWrapper<UserNotifications> update = new LambdaUpdateWrapper<>();
        update.eq(UserNotifications::getRecipientAddress, walletAddress)
              .eq(UserNotifications::getNotificationId, notificationId)
              .set(UserNotifications::getIsRead, true)
              .set(UserNotifications::getReadAt, LocalDateTime.now());
        return update(update);
    }

    @Override
    public boolean markAllAsRead(String walletAddress) {
        return baseMapper.markAllAsRead(walletAddress) > 0;
    }

    @Override
    public long getUnreadCount(String walletAddress) {
        return baseMapper.countUnread(walletAddress);
    }
}