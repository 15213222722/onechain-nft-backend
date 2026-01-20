package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("messages")
@ApiModel(value = "Messages对象", description = "消息表")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发送方id")
    @TableField("senderUserId")
    private Integer senderUserId;

    @ApiModelProperty("接收方id")
    @TableField("receiverUserId")
    private Integer receiverUserId;

    @ApiModelProperty("消息类型,text:普通文本;offer:报价;counter_offer:")
    @TableField("messageType")
    private String messageType;

    @ApiModelProperty("消息内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("nft 对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("报价金额")
    @TableField("offerAmount")
    private BigDecimal offerAmount;

    @ApiModelProperty("是否已读")
    @TableField("isRead")
    private Boolean isRead;

    @ApiModelProperty("被发送方删除")
    @TableField("deletedBySender")
    private Boolean deletedBySender;

    @ApiModelProperty("被接收方删除")
    @TableField("deletedByReceiver")
    private Boolean deletedByReceiver;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
