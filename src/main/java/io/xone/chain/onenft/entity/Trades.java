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
 * NFT 成交记录表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Getter
@Setter
@TableName("trades")
@ApiModel(value = "Trades对象", description = "NFT 成交记录表")
public class Trades implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("listing_object_id")
    private String listingObjectId;

    @ApiModelProperty("1=Sale (买NFT), 2=Swap (NFT↔NFT)")
    @TableField("trade_type")
    private Byte tradeType;

    @ApiModelProperty("主动成交方 (买家/提供换入NFT的人)")
    @TableField("taker_address")
    private String takerAddress;

    @ApiModelProperty("挂单人 (卖家/原NFT持有者)")
    @TableField("lister_address")
    private String listerAddress;

    @ApiModelProperty("给 taker 的 NFT (原挂单的)")
    @TableField("nft_object_id_out")
    private String nftObjectIdOut;

    @ApiModelProperty("给 lister 的 NFT (Swap 时才有)")
    @TableField("nft_object_id_in")
    private String nftObjectIdIn;

    @ApiModelProperty("Sale 时的支付金额 (原始值)")
    @TableField("payment_amount")
    private Long paymentAmount;

    @ApiModelProperty("Sale 时的手续费")
    @TableField("fee_amount")
    private Long feeAmount;

    @ApiModelProperty("Sale 时代理收到金额")
    @TableField("seller_amount")
    private Long sellerAmount;

    @ApiModelProperty("Sale 时的支付币种类型")
    @TableField("coin_type")
    private String coinType;

    @ApiModelProperty("集合标识 (方便聚合)")
    @TableField("collection_slug")
    private String collectionSlug;

    @ApiModelProperty("成交交易 digest")
    @TableField("tx_digest")
    private String txDigest;

    @ApiModelProperty("成交时间戳 (ms)")
    @TableField("timestamp_ms")
    private Long timestampMs;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
