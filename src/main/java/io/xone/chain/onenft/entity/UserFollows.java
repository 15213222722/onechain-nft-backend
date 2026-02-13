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
 * 用户关注表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-13
 */
@Getter
@Setter
@TableName("user_follows")
@ApiModel(value = "UserFollows对象", description = "用户关注表")
public class UserFollows implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户地址")
    @TableField("follower_wallet_address")
    private String followerWalletAddress;

    @ApiModelProperty("被关注用户地址")
    @TableField("following_wallet_address")
    private String followingWalletAddress;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
