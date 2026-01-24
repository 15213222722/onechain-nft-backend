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
 * nft表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Getter
@Setter
@TableName("nfts")
@ApiModel(value = "Nfts对象", description = "nft表")
public class Nfts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("对象id")
    @TableField("objectId")
    private String objectId;

    @ApiModelProperty("合约地址")
    @TableField("contractAddress")
    private String contractAddress;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("图片地址")
    @TableField("imageUrl")
    private String imageUrl;

    @ApiModelProperty("拥有者")
    @TableField("kioskId")
    private String kioskId;

    @ApiModelProperty("属性")
    @TableField("attributes")
    private String attributes;

    @ApiModelProperty("稀有度，common：普通；rare：稀有；epic：史诗；legendary：传说")
    @TableField("rarityType")
    private String rarityType;

    @ApiModelProperty("稀有度排名")
    @TableField("rarityRank")
    private Integer rarityRank;

    @ApiModelProperty("是否上架")
    @TableField("isListed")
    private Boolean isListed;

    @ApiModelProperty("上架价格")
    @TableField("listingPrice")
    private BigDecimal listingPrice;

    @ApiModelProperty("分类：art:数字艺术；pfp：PFP/头像；assets：游戏资产；collectibles：收藏品；utility：实用型；media：音视频；others：其他")
    @TableField("category")
    private String category;

    @ApiModelProperty("竞拍开始价格")
    @TableField("auctionStartPrice")
    private BigDecimal auctionStartPrice;

    @ApiModelProperty("竞拍最新价格")
    @TableField("auctionEndPrice")
    private BigDecimal auctionEndPrice;

    @ApiModelProperty("竞拍开始时间")
    @TableField("auctionStartTime")
    private LocalDateTime auctionStartTime;

    @ApiModelProperty("竞拍结束时间")
    @TableField("auctionEndTime")
    private LocalDateTime auctionEndTime;

    @ApiModelProperty("铸造hash")
    @TableField("mintTxHash")
    private String mintTxHash;

    @ApiModelProperty("浏览次数")
    @TableField("viewCount")
    private Integer viewCount;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

    @ApiModelProperty("所属系列")
    @TableField("seriesId")
    private Integer seriesId;

    @ApiModelProperty("创建者")
    @TableField("creatorAddress")
    private String creatorAddress;

    @ApiModelProperty("拥有者")
    @TableField("ownerAddress")
    private String ownerAddress;

    @ApiModelProperty("NFT 类型")
    @TableField("nftType")
    private String nftType;
}
