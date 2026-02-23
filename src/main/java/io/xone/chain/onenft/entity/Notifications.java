package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 通知主表：定义通知内容与基础信息
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-23
 */
@Getter
@Setter
@TableName("notifications")
@ApiModel(value = "Notifications对象", description = "通知主表：定义通知内容与基础信息")
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("通知类型，如 NFT_SOLD / USER_FOLLOWED / SYSTEM_ANNOUNCEMENT")
    @TableField("type")
    private String type;

    @ApiModelProperty("通知来源类型：USER（用户行为）或 SYSTEM（系统通知）")
    @TableField("source_type")
    private String sourceType;

    @ApiModelProperty("触发通知的用户地址（系统通知可为空）")
    @TableField("actor_address")
    private String actorAddress;

    @ApiModelProperty("通知关联对象类型：NFT / LISTING / USER")
    @TableField("target_type")
    private String targetType;

    @ApiModelProperty("通知关联对象ID，如 nft_id / listing_id / user_address")
    @TableField("target_id")
    private String targetId;

    @ApiModelProperty("通知标题（前端可直接展示）")
    @TableField("title")
    private String title;

    @ApiModelProperty("通知正文内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("扩展信息JSON，如价格、币种等附加数据")
    @TableField("metadata")
    private String metadata;

    @ApiModelProperty("优先级：0普通 1重要 2紧急")
    @TableField("priority")
    private Byte priority;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
