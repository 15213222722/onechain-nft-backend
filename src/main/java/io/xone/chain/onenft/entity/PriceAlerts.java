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
@TableName("price_alerts")
@ApiModel(value = "PriceAlerts对象", description = "")
public class PriceAlerts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("collectionName")
    private String collectionName;

    @TableField("alertType")
    private String alertType;

    @TableField("targetPrice")
    private String targetPrice;

    @TableField("priceWhenSet")
    private String priceWhenSet;

    @TableField("isActive")
    private Boolean isActive;

    @TableField("isTriggered")
    private Boolean isTriggered;

    @TableField("triggeredAt")
    private LocalDateTime triggeredAt;

    @TableField("triggeredPrice")
    private String triggeredPrice;

    @TableField("notificationSent")
    private Boolean notificationSent;

    @TableField("notes")
    private String notes;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
