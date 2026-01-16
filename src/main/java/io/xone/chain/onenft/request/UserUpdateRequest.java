package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserUpdateRequest", description = "User Update Request")
public class UserUpdateRequest {

    @ApiModelProperty(value = "Wallet Address", required = true)
    private String walletAddress;

    @ApiModelProperty(value = "Name")
    private String name;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Twitter")
    private String twitter;

    @ApiModelProperty(value = "Avatar URL")
    private String avatarUrl;

    @ApiModelProperty(value = "Description")
    private String description;
}
