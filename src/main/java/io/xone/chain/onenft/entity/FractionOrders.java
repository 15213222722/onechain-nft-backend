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
@TableName("fraction_orders")
@ApiModel(value = "FractionOrders对象", description = "")
public class FractionOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("fractionId")
    private Integer fractionId;

    @TableField("userId")
    private Integer userId;

    @TableField("orderType")
    private String orderType;

    @TableField("amount")
    private Integer amount;

    @TableField("pricePerFraction")
    private String pricePerFraction;

    @TableField("filledAmount")
    private Integer filledAmount;

    @TableField("status")
    private String status;

    @TableField("expiresAt")
    private LocalDateTime expiresAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
