package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Collection Query Request")
public class UserCollectionQueryRequest extends PageRequest {
    
    @ApiModelProperty(value = "Wallet Address (Optional, defaults to current user)", required = false)
    private String walletAddress;
}
