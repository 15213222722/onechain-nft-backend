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
 * NFT 挂单表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Getter
@Setter
@TableName("listings")
@ApiModel(value = "Listings对象", description = "NFT 挂单表")
public class Listings implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("OneChain Object ID (0x...)")
    @TableField("listing_object_id")
    private String listingObjectId;

    @ApiModelProperty("挂单人地址")
    @TableField("owner_address")
    private String ownerAddress;

    @ApiModelProperty("挂单中的 NFT Object ID")
    @TableField("nft_object_id")
    private String nftObjectId;

    @ApiModelProperty("NFT 类型全名 (type_name::with_original_ids<T>())")
    @TableField("nft_type")
    private String nftType;

    @ApiModelProperty("0=SWAP, 1=SALE")
    @TableField("listing_type")
    private Integer listingType;

    @ApiModelProperty("0=ACTIVE, 1=FILLED, 2=CANCELLED")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("Swap 时期望的 NFT 类型")
    @TableField("expected_nft_type")
    private String expectedNftType;

    @ApiModelProperty("SALE 时的价格（原始数值，需注意 decimals）")
    @TableField("price")
    private Long price;

    @ApiModelProperty("SALE 时的支付币种类型")
    @TableField("coin_type")
    private String coinType;

    @ApiModelProperty("NFT 集合标识（链下映射或从元数据获取）")
    @TableField("collection_slug")
    private String collectionSlug;

    @ApiModelProperty("集合名称（可选，便于展示）")
    @TableField("collection_name")
    private String collectionName;

    @ApiModelProperty("创建交易 digest")
    @TableField("create_tx_digest")
    private String createTxDigest;

    @ApiModelProperty("挂单被成交 digest")
    @TableField("filled_tx_digest")
    private String filledTxDigest;

    @ApiModelProperty("挂单被成交时的链上时间戳")
    @TableField("filled_timestamp_ms")
    private Long filledTimestampMs;

    @ApiModelProperty("挂单被取消 digest")
    @TableField("cancel_tx_digest")
    private String cancelTxDigest;

    @ApiModelProperty("挂单被取消时的链上时间戳")
    @TableField("cancel_timestamp_ms")
    private Long cancelTimestampMs;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
