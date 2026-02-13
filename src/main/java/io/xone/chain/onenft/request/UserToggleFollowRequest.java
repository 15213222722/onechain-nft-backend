package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("User Toggle Follow Request")
public class UserToggleFollowRequest {
    
    @ApiModelProperty(value = "Following Address", required = true)
    @NotBlank
    private String followingAddress;

    @ApiModelProperty(value = "Is Follow (true=follow, false=unfollow)", required = true)
    @NotNull
    private Boolean isFollow;
}
