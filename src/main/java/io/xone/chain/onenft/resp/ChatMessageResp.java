package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("Chat Message Response")
public class ChatMessageResp {

    @ApiModelProperty("Message ID")
    private Long id;

    @ApiModelProperty("Conversation ID")
    private Long chatConversationsId;

    @ApiModelProperty("Sender Address")
    private String fromAddress;

    @ApiModelProperty("Receiver Address")
    private String toAddress;

    @ApiModelProperty("Message Type")
    private Byte msgType;

    @ApiModelProperty("Message Content")
    private String content;

    @ApiModelProperty("Extra Data")
    private String extra;

    @ApiModelProperty("Is Read")
    private Boolean isRead;

    @ApiModelProperty("Created At")
    private LocalDateTime createdAt;
}
