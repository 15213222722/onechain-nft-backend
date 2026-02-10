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
 * 下架事件表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@Getter
@Setter
@TableName("nft_delist_event")
@ApiModel(value = "NftDelistEvent对象", description = "下架事件表")
public class NftDelistEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("钱包地址")
    @TableField("wallet_address")
    private String walletAddress;

    @ApiModelProperty("nft对象id")
    @TableField("nft_object_id")
    private String nftObjectId;

    @ApiModelProperty("市场id")
    @TableField("listing_object_id")
    private String listingObjectId;

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
