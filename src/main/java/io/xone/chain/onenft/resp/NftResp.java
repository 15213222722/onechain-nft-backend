package io.xone.chain.onenft.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "NFT Response", description = "NFT Information Response")
public class NftResp {

    @ApiModelProperty("Object ID")
    private String objectId;

    @ApiModelProperty("Contract Address")
    private String contractAddress;

    @ApiModelProperty("Name")
    private String name;
    
    @ApiModelProperty("拥有者")
    private String kioskId;

    @ApiModelProperty("Description")
    private String description;

    @ApiModelProperty("Image URL")
    private String imageUrl;

    @ApiModelProperty("Owner ID")
    private Integer ownerId;

    @ApiModelProperty("Owner Name")
    private String ownerName;

    @ApiModelProperty("Creator ID")
    private Integer creatorId;

    @ApiModelProperty("Creator Name")
    private String creatorName;

    @ApiModelProperty("Attributes")
    private String attributes;

    @ApiModelProperty("Rarity Type")
    private String rarityType;

    @ApiModelProperty("Rarity Rank")
    private Integer rarityRank;

    @ApiModelProperty("Is Listed")
    private Boolean isListed;

    @ApiModelProperty("Listing Price")
    private BigDecimal listingPrice;

    @ApiModelProperty("Category")
    private String category;

    @ApiModelProperty("Series ID")
    private Integer seriesId;

    @ApiModelProperty("Series Name")
    private String seriesName;

    @ApiModelProperty("Auction Start Price")
    private BigDecimal auctionStartPrice;

    @ApiModelProperty("Auction End Price")
    private BigDecimal auctionEndPrice;

    @ApiModelProperty("Auction Start Time")
    private LocalDateTime auctionStartTime;

    @ApiModelProperty("Auction End Time")
    private LocalDateTime auctionEndTime;

    @ApiModelProperty("Created At")
    private LocalDateTime createdAt;

    @ApiModelProperty("Updated At")
    private LocalDateTime updatedAt;
    
    @ApiModelProperty("创建者")
    private String creatorAddress;

    @ApiModelProperty("拥有者")
    private String ownerAddress;
    
    @ApiModelProperty("NFT 类型")
    private String nftType;
}