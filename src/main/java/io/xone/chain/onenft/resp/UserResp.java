package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "User Response", description = "User Information Response")
public class UserResp {

    @ApiModelProperty(value = "User ID")
    private Integer id;

    @ApiModelProperty(value = "Wallet Address")
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

    @ApiModelProperty(value = "Role")
    private String role;

    @ApiModelProperty(value = "Created At")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "Updated At")
    private LocalDateTime updatedAt;
}
