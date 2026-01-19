package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "NFT Response", description = "NFT Information Response")
public class NftResp {

    @ApiModelProperty("Object ID")
    private String objectId;

    @ApiModelProperty("Contract Address")
    private String contractAddress;

    @ApiModelProperty("Name")
    private String name;

    @ApiModelProperty("Description")
    private String description;

    @ApiModelProperty("Image URL")
    private String imageUrl;

    @ApiModelProperty("Owner ID")
    private Integer ownerId;

    @ApiModelProperty("Creator ID")
    private Integer creatorId;

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
}
