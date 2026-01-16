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
@TableName("royalty_payments")
@ApiModel(value = "RoyaltyPayments对象", description = "")
public class RoyaltyPayments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("royaltyId")
    private Integer royaltyId;

    @TableField("saleTxHash")
    private String saleTxHash;

    @TableField("tokenId")
    private String tokenId;

    @TableField("salePrice")
    private String salePrice;

    @TableField("royaltyAmount")
    private String royaltyAmount;

    @TableField("sellerAddress")
    private String sellerAddress;

    @TableField("buyerAddress")
    private String buyerAddress;

    @TableField("status")
    private String status;

    @TableField("paymentTxHash")
    private String paymentTxHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
