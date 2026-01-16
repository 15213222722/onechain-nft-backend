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
 * 
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Getter
@Setter
@TableName("user_achievements")
@ApiModel(value = "UserAchievements对象", description = "")
public class UserAchievements implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("achievementType")
    private String achievementType;

    @TableField("tier")
    private String tier;

    @TableField("progress")
    private Integer progress;

    @TableField("target")
    private Integer target;

    @TableField("isCompleted")
    private Boolean isCompleted;

    @TableField("completedAt")
    private LocalDateTime completedAt;

    @TableField("metadata")
    private String metadata;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
