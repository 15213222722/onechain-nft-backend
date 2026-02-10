package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 上架事件表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
@Getter
@Setter
@TableName("nft_listing_event")
@ApiModel(value = "NftListingEvent对象", description = "上架事件表")
public class NftListingEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("钱包地址")
    @TableField("walletAddress")
    private String walletAddress;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("上架id")
    @TableField("listing_Object_id")
    private String listingObjectId;

    @ApiModelProperty("hash")
    @TableField("txHash")
    private String txHash;

    @ApiModelProperty("事件type")
    @TableField("eventType")
    private String eventType;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

    @ApiModelProperty("上架价格")
    @TableField("listingPrice")
    private long listingPrice;
}
