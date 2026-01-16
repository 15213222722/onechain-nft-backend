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
@TableName("conversations")
@ApiModel(value = "Conversations对象", description = "")
public class Conversations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("participant1Id")
    private Integer participant1Id;

    @TableField("participant2Id")
    private Integer participant2Id;

    @TableField("lastMessageId")
    private Integer lastMessageId;

    @TableField("lastMessagePreview")
    private String lastMessagePreview;

    @TableField("unreadCount1")
    private Integer unreadCount1;

    @TableField("unreadCount2")
    private Integer unreadCount2;

    @TableField("archived1")
    private Boolean archived1;

    @TableField("archived2")
    private Boolean archived2;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
