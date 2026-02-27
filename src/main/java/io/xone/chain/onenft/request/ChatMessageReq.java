package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("Send Chat Message Request")
public class ChatMessageReq {

    @ApiModelProperty(value = "Receiver Wallet Address", required = true)
    @NotBlank
    private String toAddress;

    @ApiModelProperty(value = "Message Content", required = true)
    @NotBlank
    private String content;

    @ApiModelProperty(value = "Message Type (1=Text, 2=Image, etc.)", required = true)
    @NotNull
    private Byte msgType;

    @ApiModelProperty(value = "Extra Data (JSON)", required = false)
    private String extra;
}
