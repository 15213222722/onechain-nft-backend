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
@TableName("batch_listings")
@ApiModel(value = "BatchListings对象", description = "")
public class BatchListings implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("status")
    private String status;

    @TableField("totalItems")
    private Integer totalItems;

    @TableField("successCount")
    private Integer successCount;

    @TableField("failedCount")
    private Integer failedCount;

    @TableField("defaultPrice")
    private String defaultPrice;

    @TableField("pricingStrategy")
    private String pricingStrategy;

    @TableField("duration")
    private Integer duration;

    @TableField("txHash")
    private String txHash;

    @TableField("errorMessage")
    private String errorMessage;

    @TableField("itemsData")
    private String itemsData;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
