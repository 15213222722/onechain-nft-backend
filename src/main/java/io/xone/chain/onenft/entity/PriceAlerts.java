package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 价格提醒表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("price_alerts")
@ApiModel(value = "PriceAlerts对象", description = "价格提醒表")
public class PriceAlerts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("提醒类型,below:低于（价格下跌至低于预警范围）;above:高于（价格上涨至高于预警范围）")
    @TableField("alertType")
    private String alertType;

    @ApiModelProperty("目标价格")
    @TableField("targetPrice")
    private BigDecimal targetPrice;

    @ApiModelProperty("设置提醒时的当前价格")
    @TableField("priceWhenSet")
    private BigDecimal priceWhenSet;

    @ApiModelProperty("是否生效")
    @TableField("isActive")
    private Boolean isActive;

    @ApiModelProperty("是否触发")
    @TableField("isTriggered")
    private Boolean isTriggered;

    @ApiModelProperty("触发时间")
    @TableField("triggeredAt")
    private LocalDateTime triggeredAt;

    @ApiModelProperty("触发时价格")
    @TableField("triggeredPrice")
    private BigDecimal triggeredPrice;

    @ApiModelProperty("通知是否已发送")
    @TableField("notificationSent")
    private Boolean notificationSent;

    @ApiModelProperty("提醒内容")
    @TableField("notes")
    private String notes;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
