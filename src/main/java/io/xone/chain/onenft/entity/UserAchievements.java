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
 * 用户成就表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("user_achievements")
@ApiModel(value = "UserAchievements对象", description = "用户成就表")
public class UserAchievements implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("成就类型")
    @TableField("achievementType")
    private String achievementType;

    @ApiModelProperty("成就等级;bronze:青铜;silver:白银;gold:黄金;platinum:铂金;diamond:钻石")
    @TableField("tier")
    private String tier;

    @ApiModelProperty("当前进度")
    @TableField("progress")
    private Integer progress;

    @ApiModelProperty("目标进度")
    @TableField("target")
    private Integer target;

    @ApiModelProperty("是否完成成就")
    @TableField("isCompleted")
    private Boolean isCompleted;

    @ApiModelProperty("完成时间")
    @TableField("completedAt")
    private LocalDateTime completedAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
