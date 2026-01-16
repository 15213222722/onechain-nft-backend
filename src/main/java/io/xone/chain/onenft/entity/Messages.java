package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Getter
@Setter
@TableName("messages")
@ApiModel(value = "Messages对象", description = "")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("senderId")
    private Integer senderId;

    @TableField("receiverId")
    private Integer receiverId;

    @TableField("messageType")
    private String messageType;

    @TableField("content")
    private String content;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("offerAmount")
    private String offerAmount;

    @TableField("isRead")
    private Boolean isRead;

    @TableField("deletedBySender")
    private Boolean deletedBySender;

    @TableField("deletedByReceiver")
    private Boolean deletedByReceiver;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
