package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User Collection Rank Request")
public class UserCollectionRankRequest {
    
    @ApiModelProperty(value = "Time Range (24H, 7D, 30D, ALL)", required = true)
    private String timeRange;

}
