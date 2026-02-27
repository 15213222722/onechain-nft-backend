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
 * 1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等）
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@Getter
@Setter
@TableName("chat_conversations")
@ApiModel(value = "ChatConversations对象", description = "1对1聊天会话（两人唯一会话记录，包含未读数、最后消息预览等）")
public class ChatConversations implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("会话自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("参与者A的地址")
    @TableField("wallet_address_a")
    private String walletAddressA;

    @ApiModelProperty("参与者B的地址")
    @TableField("wallet_address_b")
    private String walletAddressB;

    @ApiModelProperty("本会话最后一条消息的ID（用于快速定位最新消息）")
    @TableField("last_msg_id")
    private Long lastMsgId;

    @ApiModelProperty("最后一条消息的预览文本（截断后显示在会话列表，通常前 50~80 个字符）")
    @TableField("last_msg_preview")
    private String lastMsgPreview;

    @ApiModelProperty("最后一条消息的发送时间（用于会话列表排序）")
    @TableField("last_msg_time")
    private LocalDateTime lastMsgTime;

    @ApiModelProperty("user_id_a 的未读消息数量（对方发给他的未读数）")
    @TableField("unread_a")
    private Integer unreadA;

    @ApiModelProperty("user_id_b 的未读消息数量（对方发给他的未读数）")
    @TableField("unread_b")
    private Integer unreadB;

    @ApiModelProperty("会话首次创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("会话最后更新时间（通常跟随最后一条消息）")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
