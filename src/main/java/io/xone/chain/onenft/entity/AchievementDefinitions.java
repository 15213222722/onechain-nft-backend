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
@TableName("achievement_definitions")
@ApiModel(value = "AchievementDefinitions对象", description = "")
public class AchievementDefinitions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("type")
    private String type;

    @TableField("category")
    private String category;

    @TableField("name")
    private String name;

    @TableField("nameZh")
    private String nameZh;

    @TableField("description")
    private String description;

    @TableField("descriptionZh")
    private String descriptionZh;

    @TableField("icon")
    private String icon;

    @TableField("badgeColor")
    private String badgeColor;

    @TableField("tierThresholds")
    private String tierThresholds;

    @TableField("pointsPerTier")
    private String pointsPerTier;

    @TableField("isActive")
    private Boolean isActive;

    @TableField("displayOrder")
    private Integer displayOrder;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
