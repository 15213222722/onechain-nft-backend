package io.xone.chain.onenft.resp;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User Response", description = "User Information Response")
public class UserResp {

    @ApiModelProperty(value = "User ID")
    private Integer id;

    @ApiModelProperty(value = "Wallet Address")
    private String walletAddress;

    @ApiModelProperty(value = "Name")
    private String name;
    
    @ApiModelProperty("kioskId")
    private String kioskId;
    
    @ApiModelProperty("capId")
    private String capId;

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
