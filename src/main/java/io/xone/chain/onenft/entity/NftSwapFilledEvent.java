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
 * 交换事件表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
@Getter
@Setter
@TableName("nft_swap_filled_event")
@ApiModel(value = "NftSwapFilledEvent对象", description = "交换事件表")
public class NftSwapFilledEvent implements Serializable {

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

    @ApiModelProperty("listing 中原有的 NFT（给 taker）")
    @TableField("nft_object_id_out")
    private String nftObjectIdOut;

    @ApiModelProperty("taker 提供的换入 NFT（给 lister）")
    @TableField("nft_object_id_in")
    private String nftObjectIdIn;

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
