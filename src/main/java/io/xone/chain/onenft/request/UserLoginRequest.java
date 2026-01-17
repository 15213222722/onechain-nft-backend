package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.common.validator.EnumValue;
import io.xone.chain.onenft.enums.LoginTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "UserLoginRequest", description = "User Login Request")
public class UserLoginRequest {

    @NotBlank(message = "Wallet Address cannot be empty")
    @ApiModelProperty(value = "Wallet Address", required = true)
    private String walletAddress;

    @EnumValue(enumClass = LoginTypeEnum.class, method = "getCode", message = "Invalid login device")
    @ApiModelProperty(value = "Login Device Type: PC, ANDROID, IOS, H5")
    private String loginDevice;
}