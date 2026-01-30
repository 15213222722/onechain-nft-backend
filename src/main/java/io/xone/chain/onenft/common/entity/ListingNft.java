package io.xone.chain.onenft.common.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingNft implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对象id")
    private String objectId;
    
    @ApiModelProperty("NFT 类型")
    private String nftType;
    
    @ApiModelProperty("名称")
    private String name;
    
    @ApiModelProperty("描述")
    private String description;
    
    @ApiModelProperty("图片地址")
    private String imageUrl;
    
    @ApiModelProperty("属性")
    private String attributes;
    
    @ApiModelProperty("创建者")
    private String creatorAddress;
    
    @ApiModelProperty("拥有者")
    private String ownerAddress;
    
    @ApiModelProperty("铸造hash")
    private String mintTxHash;
    
}

