package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User Collection Rank Response")
public class UserCollectionRankResp {
    @ApiModelProperty("Wallet Address")
    private String walletAddress;
    
    @ApiModelProperty("User Name")
    private String name;
    
    @ApiModelProperty("Avatar URL")
    private String avatarUrl;
    
    @ApiModelProperty("Collection Count")
    private Long collectionCount;
}
