package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Pagination Request")
public class PageRequest {
    @ApiModelProperty(value = "Current Page", example = "1")
    private long current = 1;

    @ApiModelProperty(value = "Page Size", example = "10")
    private long size = 10;
}
