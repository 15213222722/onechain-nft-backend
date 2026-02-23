package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Notification Query Request")
public class NotificationQueryRequest extends PageRequest {
    
    @ApiModelProperty(value = "Is Read (null for all)", required = false)
    private Boolean isRead;
}
