package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户通知关系表：记录通知接收人与已读状态
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-23
 */
@Getter
@Setter
@TableName("user_notifications")
@ApiModel(value = "UserNotifications对象", description = "用户通知关系表：记录通知接收人与已读状态")
public class UserNotifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("通知ID，对应 notifications.id")
    @TableId("notification_id")
    private Integer notificationId;

    @ApiModelProperty("接收通知的用户地址")
    @TableId("recipient_address")
    private String recipientAddress;

    @ApiModelProperty("是否已读：0未读 1已读")
    @TableField("is_read")
    private Boolean isRead;

    @ApiModelProperty("已读时间")
    @TableField("read_at")
    private LocalDateTime readAt;
}
