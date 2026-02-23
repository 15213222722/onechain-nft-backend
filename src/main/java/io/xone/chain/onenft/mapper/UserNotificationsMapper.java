package io.xone.chain.onenft.mapper;

import io.xone.chain.onenft.entity.UserNotifications;
import io.xone.chain.onenft.resp.NotificationResp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户通知关系表：记录通知接收人与已读状态 Mapper 接口
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-23
 */
public interface UserNotificationsMapper extends BaseMapper<UserNotifications> {

    @Select("<script>" +
            "SELECT n.*, un.is_read as isRead, un.read_at as readAt " +
            "FROM notifications n " +
            "JOIN user_notifications un ON n.id = un.notification_id " +
            "WHERE un.recipient_address = #{walletAddress} " +
            "<if test='isRead != null'>" +
            "  AND un.is_read = #{isRead} " +
            "</if>" +
            "ORDER BY n.created_at DESC" +
            "</script>")
    Page<NotificationResp> getUserNotifications(Page<NotificationResp> page, @Param("walletAddress") String walletAddress, @Param("isRead") Boolean isRead);

    @Select("SELECT COUNT(*) FROM user_notifications WHERE recipient_address = #{walletAddress} AND is_read = 0")
    long countUnread(@Param("walletAddress") String walletAddress);
    
    @Update("UPDATE user_notifications SET is_read = 1, read_at = NOW() WHERE recipient_address = #{walletAddress} AND is_read = 0")
    int markAllAsRead(@Param("walletAddress") String walletAddress);
}