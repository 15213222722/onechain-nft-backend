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
 * 售卖事件表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
@Getter
@Setter
@TableName("nft_sale_filled_event")
@ApiModel(value = "NftSaleFilledEvent对象", description = "售卖事件表")
public class NftSaleFilledEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("nft对象id")
    @TableField("nft_object_id")
    private String nftObjectId;

    @ApiModelProperty("市场id")
    @TableField("listing_object_id")
    private String listingObjectId;

    @ApiModelProperty("买家地址")
    @TableField("taker")
    private String taker;

    @ApiModelProperty("卖家（挂单人）地址")
    @TableField("lister")
    private String lister;

    @ApiModelProperty("买家实际支付的 Coin 数量（原始 value）")
    @TableField("payment_amount")
    private Long paymentAmount;

    @ApiModelProperty("卖家实际收到金额")
    @TableField("seller_amount")
    private Long sellerAmount;

    @ApiModelProperty("实际扣除的手续费")
    @TableField("fee_amount")
    private Long feeAmount;

    @ApiModelProperty("支付的币种")
    @TableField("coin_type")
    private String coinType;

    @ApiModelProperty("hash")
    @TableField("tx_hash")
    private String txHash;

    @ApiModelProperty("事件type")
    @TableField("event_type")
    private String eventType;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
