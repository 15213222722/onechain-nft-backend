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
 * 
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-17
 */
@Getter
@Setter
@TableName("nfts")
@ApiModel(value = "Nfts对象", description = "")
public class Nfts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("objectId")
    private String objectId;

    @TableField("contractAddress")
    private String contractAddress;

    @ApiModelProperty("name")
    @TableField("name")
    private String name;

    @ApiModelProperty("description")
    @TableField("description")
    private String description;

    @ApiModelProperty("image")
    @TableField("imageUrl")
    private String imageUrl;

    @TableField("ownerId")
    private Integer ownerId;

    @TableField("creatorId")
    private Integer creatorId;

    @TableField("attributes")
    private String attributes;

    @TableField("rarityScore")
    private String rarityScore;

    @TableField("rarityRank")
    private Integer rarityRank;

    @TableField("isListed")
    private Boolean isListed;

    @TableField("listingPrice")
    private String listingPrice;

    @TableField("listingType")
    private String listingType;

    @TableField("auctionStartPrice")
    private String auctionStartPrice;

    @TableField("auctionEndPrice")
    private String auctionEndPrice;

    @TableField("auctionStartTime")
    private LocalDateTime auctionStartTime;

    @TableField("auctionEndTime")
    private LocalDateTime auctionEndTime;

    @TableField("royaltyPercentage")
    private String royaltyPercentage;

    @TableField("mintTxHash")
    private String mintTxHash;

    @TableField("viewCount")
    private Integer viewCount;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
