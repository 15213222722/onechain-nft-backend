package io.xone.chain.onenft.resp;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.common.entity.ListingNft;
import lombok.Data;

@Data
@ApiModel(value = "Lising NFT Response", description = "Lising NFT Information Response")
public class ListingResp {
	
	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("OneChain Object ID (0x...)")
    private String listingObjectId;

    @ApiModelProperty("挂单人地址")
    private String ownerAddress;

    @ApiModelProperty("挂单中的 NFT Object ID")
    private String nftObjectId;

    @ApiModelProperty("NFT 类型全名 (type_name::with_original_ids<T>())")
    private String nftType;

    @ApiModelProperty("0=SWAP, 1=SALE")
    private Integer listingType;

    @ApiModelProperty("0=ACTIVE, 1=FILLED, 2=CANCELLED")
    private Integer status;

    @ApiModelProperty("Swap 时期望的 NFT 类型")
    private String expectedNftType;

    @ApiModelProperty("SALE 时的价格（原始数值，需注意 decimals）")
    private Long price;

    @ApiModelProperty("SALE 时的支付币种类型")
    private String coinType;

    @ApiModelProperty("NFT 集合标识（链下映射或从元数据获取）")
    private String collectionSlug;

    @ApiModelProperty("集合名称（可选，便于展示）")
    private String collectionName;

    @ApiModelProperty("创建交易 digest")
    private String createTxDigest;

    @ApiModelProperty("挂单被成交 digest")
    private String filledTxDigest;

    @ApiModelProperty("挂单被成交时的链上时间戳")
    private Long filledTimestampMs;

    @ApiModelProperty("挂单被取消 digest")
    private String cancelTxDigest;

    @ApiModelProperty("挂单被取消时的链上时间戳")
    private Long cancelTimestampMs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    
    private ListingNft listingNft;

}
