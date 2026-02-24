package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.Notifications;
import io.xone.chain.onenft.entity.UserNotifications;
import io.xone.chain.onenft.enums.NotificationType;
import io.xone.chain.onenft.mapper.NotificationsMapper;
import io.xone.chain.onenft.service.INotificationsService;
import io.xone.chain.onenft.service.IUserNotificationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统通知表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl extends ServiceImpl<NotificationsMapper, Notifications> implements INotificationsService {

    private final IUserNotificationsService userNotificationsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(NotificationType type, String actorAddress, String recipientAddress,
                          String targetType, String targetId, Object... args) {

        Notifications notif = new Notifications();
        notif.setType(type.name());
        notif.setSourceType("USER");
        notif.setActorAddress(actorAddress);
        notif.setTargetType(targetType);
        notif.setTargetId(targetId);
        notif.setTitle(type.formatTitle(args));
        notif.setContent(type.formatContent(args));
        notif.setPriority((byte)0);
        notif.setCreatedAt(LocalDateTime.now());
        notif.setUpdatedAt(LocalDateTime.now());

        this.save(notif);

        UserNotifications userNotif = new UserNotifications();
        userNotif.setNotificationId(notif.getId());
        userNotif.setRecipientAddress(recipientAddress);
        userNotif.setIsRead(false);
        userNotificationsService.save(userNotif);
    }
}