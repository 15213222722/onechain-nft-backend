package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.Notifications;
import io.xone.chain.onenft.enums.NotificationType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统通知表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
public interface INotificationsService extends IService<Notifications> {

    /**
     * Create and send notification
     * 
     * @param type Notification Type
     * @param actorAddress Who triggered the notification
     * @param recipientAddress Who receives the notification
     * @param targetType Target entity type (NFT, USER, LISTING)
     * @param targetId Target entity ID
     * @param args Arguments for template formatting
     */
    void createNotification(NotificationType type, String actorAddress, String recipientAddress, 
                          String targetType, String targetId, Object... args);

}