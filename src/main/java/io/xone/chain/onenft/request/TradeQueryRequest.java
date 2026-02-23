package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Trade Query Request")
public class TradeQueryRequest extends PageRequest {

    @ApiModelProperty(value = "Time Range (24H, 7D, 30D, ALL)", required = false)
    private String timeRange;

    @ApiModelProperty(value = "Collection Slug (Optional)", required = false)
    private String collectionSlug;
    
    @ApiModelProperty(value = "NFT Object ID (Optional)", required = false)
    private String nftObjectId;
}
