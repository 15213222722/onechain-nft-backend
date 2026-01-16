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
@TableName("fraction_transactions")
@ApiModel(value = "FractionTransactions对象", description = "")
public class FractionTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("fractionId")
    private Integer fractionId;

    @TableField("buyOrderId")
    private Integer buyOrderId;

    @TableField("sellOrderId")
    private Integer sellOrderId;

    @TableField("buyerId")
    private Integer buyerId;

    @TableField("sellerId")
    private Integer sellerId;

    @TableField("amount")
    private Integer amount;

    @TableField("pricePerFraction")
    private String pricePerFraction;

    @TableField("totalPrice")
    private String totalPrice;

    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
