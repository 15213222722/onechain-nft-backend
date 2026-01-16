package io.xone.chain.onenft.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserLoginDto", description = "User Login Request")
public class UserLoginDto {

    @ApiModelProperty(value = "Wallet Address", required = true)
    private String walletAddress;
}
