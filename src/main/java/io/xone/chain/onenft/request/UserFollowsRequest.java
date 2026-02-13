package io.xone.chain.onenft.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("User Follows Request")
public class UserFollowsRequest extends PageRequest {
    
    @ApiModelProperty(value = "User Wallet Address", required = true)
    @NotBlank
    private String walletAddress;
}
