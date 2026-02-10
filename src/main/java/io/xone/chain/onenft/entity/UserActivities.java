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
 * 用户活动记录表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@Getter
@Setter
@TableName("user_activities")
@ApiModel(value = "UserActivities对象", description = "用户活动记录表")
public class UserActivities implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("activity id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("行为发起者 address")
    @TableField("actor_address")
    private String actorAddress;

    @ApiModelProperty("行为类型")
    @TableField("activity_type")
    private String activityType;

    @ApiModelProperty("目标类型: NFT | LISTING | USER")
    @TableField("target_type")
    private String targetType;

    @ApiModelProperty("目标 ID（nft_id / listing_id / user address）")
    @TableField("target_id")
    private String targetId;

    @ApiModelProperty("行为附加信息")
    @TableField("metadata")
    private String metadata;

    @ApiModelProperty("链上交易 digest（仅链上行为）")
    @TableField("tx_digest")
    private String txDigest;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
