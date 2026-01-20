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
 * 成就定义表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("achievement_definitions")
@ApiModel(value = "AchievementDefinitions对象", description = "成就定义表")
public class AchievementDefinitions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("成就类型")
    @TableField("type")
    private String type;

    @ApiModelProperty("分类,collector:收藏家;trader:交易;creator:铸造;social:社交;special:特殊")
    @TableField("category")
    private String category;

    @ApiModelProperty("成就英文名称")
    @TableField("nameEn")
    private String nameEn;

    @ApiModelProperty("成就中文名称")
    @TableField("nameZh")
    private String nameZh;

    @ApiModelProperty("成就英文描述")
    @TableField("descriptionEn")
    private String descriptionEn;

    @ApiModelProperty("成就中文描述")
    @TableField("descriptionZh")
    private String descriptionZh;

    @ApiModelProperty("徽章标识")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("徽章颜色/样式")
    @TableField("badgeColor")
    private String badgeColor;

    @ApiModelProperty("段位阈值（JSON 格式）：{ bronze: 1, silver: 10, gold: 50, platinum: 100, diamond: 500 }")
    @TableField("tierThresholds")
    private String tierThresholds;

    @ApiModelProperty("每个等级奖励的积分/经验值")
    @TableField("pointsPerTier")
    private String pointsPerTier;

    @ApiModelProperty("是否生效")
    @TableField("isActive")
    private Boolean isActive;

    @ApiModelProperty("显示顺序")
    @TableField("displayOrder")
    private Integer displayOrder;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
