package io.xone.chain.onenft.resp;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User Activity Response")
public class UserActivityResp {

    private Long id;
    
    @ApiModelProperty("Actor Address")
    private String actorAddress;
    
    @ApiModelProperty("Actor Name")
    private String actorName;
    
    @ApiModelProperty("Activity Type")
    private String activityType;
    
    @ApiModelProperty("Target Type")
    private String targetType;
    
    @ApiModelProperty("Target ID")
    private String targetId;
    
    @ApiModelProperty("链上交易 digest（仅链上行为）")
    private String txDigest;
    
    @ApiModelProperty("Created At")
    private LocalDateTime createdAt;
    
    @ApiModelProperty("Metadata JSON")
    private String metadata;
}
