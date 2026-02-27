package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Chat Mark Read Request")
public class ChatReadReq {

    @ApiModelProperty(value = "Conversation ID", required = true)
    @NotNull
    private Long conversationId;
}
