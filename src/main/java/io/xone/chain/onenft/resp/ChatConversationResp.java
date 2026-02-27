package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("Chat Conversation Response")
public class ChatConversationResp {
    
    @ApiModelProperty("Conversation ID")
    private Long id;
    
    @ApiModelProperty("Other User Address")
    private String otherAddress;
    
    @ApiModelProperty("Other User Name")
    private String otherName;
    
    @ApiModelProperty("Other User Avatar")
    private String otherAvatar;
    
    @ApiModelProperty("Other User isOnline")
    private boolean otherIsOnline;
    
    @ApiModelProperty("Last Message Preview")
    private String lastMsgPreview;
    
    @ApiModelProperty("Last Message Time")
    private LocalDateTime lastMsgTime;
    
    @ApiModelProperty("Unread Count for Current User")
    private Integer unreadCount;
}
