package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("User Collection Request")
public class UserCollectionRequest {

    @ApiModelProperty(value = "NFT Object ID", required = true)
    @NotBlank
    private String nftObjectId;
}
