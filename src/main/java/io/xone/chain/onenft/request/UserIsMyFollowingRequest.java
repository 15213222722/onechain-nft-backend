package io.xone.chain.onenft.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User Is My Following Request")
public class UserIsMyFollowingRequest {

	@ApiModelProperty(value = "User Wallet Address", required = true)
	@NotBlank
	private String walletAddress;
}
