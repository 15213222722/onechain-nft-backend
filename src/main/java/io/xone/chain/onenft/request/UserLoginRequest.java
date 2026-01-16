package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserLoginRequest", description = "User Login Request")
public class UserLoginRequest {

    @ApiModelProperty(value = "Wallet Address", required = true)
    private String walletAddress;
}
