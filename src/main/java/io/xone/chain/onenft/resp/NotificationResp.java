package io.xone.chain.onenft.resp;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Notification Response")
public class NotificationResp {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("Type")
    private String type;

    @ApiModelProperty("Source Type (USER/SYSTEM)")
    private String sourceType;

    @ApiModelProperty("Actor Address")
    private String actorAddress;

    @ApiModelProperty("Target Type")
    private String targetType;

    @ApiModelProperty("Target ID")
    private String targetId;

    @ApiModelProperty("Title")
    private String title;

    @ApiModelProperty("Content")
    private String content;

    @ApiModelProperty("Metadata (JSON)")
    private String metadata;

    @ApiModelProperty("Priority")
    private Byte priority;

    @ApiModelProperty("Created At")
    private LocalDateTime createdAt;
    
    @ApiModelProperty("Is Read")
    private Boolean isRead;

    @ApiModelProperty("Read At")
    private LocalDateTime readAt;
}
