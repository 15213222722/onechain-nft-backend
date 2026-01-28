package io.xone.chain.onenft.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserInfoRequest", description = "User Info Request")
public class UserInfoRequest {
	
    @ApiModelProperty(value = "Wallet Address", required = true)
    @NotBlank(message = "Wallet Address cannot be blank")
    private String walletAddress;

}
