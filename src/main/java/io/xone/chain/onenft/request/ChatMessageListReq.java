package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Chat Message List Request")
public class ChatMessageListReq extends PageRequest {

    @ApiModelProperty(value = "Conversation ID", required = true)
    @NotNull
    private Long conversationId;
}
