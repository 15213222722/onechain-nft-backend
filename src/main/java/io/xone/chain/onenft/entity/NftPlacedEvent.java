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
 * nft放入市场事件
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Getter
@Setter
@TableName("nft_placed__event")
@ApiModel(value = "NftPlacedEvent对象", description = "nft放入市场事件")
public class NftPlacedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("钱包地址")
    @TableField("walletAddress")
    private String walletAddress;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("市场id")
    @TableField("kioskId")
    private String kioskId;

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
}
