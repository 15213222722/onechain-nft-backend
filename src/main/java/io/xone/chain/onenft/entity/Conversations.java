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
 * 对话表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("conversations")
@ApiModel(value = "Conversations对象", description = "对话表")
public class Conversations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("参与者1")
    @TableField("participant1Id")
    private Integer participant1Id;

    @ApiModelProperty("参与者2")
    @TableField("participant2Id")
    private Integer participant2Id;

    @ApiModelProperty("上一条消息id")
    @TableField("lastMessageId")
    private Integer lastMessageId;

    @ApiModelProperty("最后一条消息预览")
    @TableField("lastMessagePreview")
    private String lastMessagePreview;

    @ApiModelProperty("未读条数1")
    @TableField("unreadCount1")
    private Integer unreadCount1;

    @ApiModelProperty("未读条数2")
    @TableField("unreadCount2")
    private Integer unreadCount2;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
