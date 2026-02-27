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
 * 1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等）
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-27
 */
@Getter
@Setter
@TableName("chat_messages")
@ApiModel(value = "ChatMessages对象", description = "1对1私聊消息记录（支持文本、图片、NFT卡片、报价、订单关联等）")
public class ChatMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属会话ID（外键指向 chat_conversations.id）")
    @TableField("chat_conversations_id")
    private Long chatConversationsId;

    @ApiModelProperty("消息发送者地址")
    @TableField("from_address")
    private String fromAddress;

    @ApiModelProperty("消息接收者地址（冗余字段，方便按地址查询）")
    @TableField("to_address")
    private String toAddress;

    @ApiModelProperty("消息类型: " +
            "1 = 普通文本, " +
            "2 = 单张图片, " +
            "3 = 多张图片（content 为 JSON 数组）, " +
            "4 = NFT 卡片（展示某个 NFT 信息）, " +
            "5 = 报价 / 出价（quote offer）, " +
            "6 = 订单相关操作（创建、接受、拒绝、支付、完成等）, " +
            "7 = 系统提示消息（已读回执、交易完成提醒等）, " +
            "8 = 撤回的消息占位（未来支持撤回时使用）")
    @TableField("msg_type")
    private Byte msgType;

    @ApiModelProperty("消息正文: " +
            "- type=1：纯文本; " +
            "- type=2：图片直链 或 object storage key; " +
            "- type=3：JSON 数组，例如：[{\"url\":\"...\",\"width\":1200,\"height\":800,\"size\":245760},...]; " +
            "- type=4：NFT 卡片 JSON，例如：{\"nft_id\":12345,\"name\":\"Azuki #8888\",\"image_url\":\"...\",\"price\":1.2,\"currency\":\"ETH\"}; " +
            "- type=5：报价 JSON，例如：{\"amount\":2.5,\"currency\":\"ETH\",\"expire_at\":\"2026-03-15T12:00:00Z\",\"remark\":\"可小刀\"}; " +
            "- type=6：订单动作 JSON，例如：{\"order_id\":80001,\"action\":\"accepted\",\"amount\":2.5}; " +
            "- type=7：系统提示纯文本")
    @TableField("content")
    private String content;

    @ApiModelProperty("扩展信息（备用字段，存放临时或未来新增的结构化数据）")
    @TableField("extra")
    private String extra;

    @ApiModelProperty("接收方是否已读（0=未读，1=已读）")
    @TableField("is_read")
    private Boolean isRead;

    @ApiModelProperty("对方首次阅读该消息的时间（用于“已读”显示）")
    @TableField("read_at")
    private LocalDateTime readAt;

    @ApiModelProperty("发送方是否删除该消息（0=正常，1=发送方已删除）")
    @TableField("deleted_by_from")
    private Boolean deletedByFrom;

    @ApiModelProperty("接收方是否删除该消息（0=正常，1=接收方已删除）")
    @TableField("deleted_by_to")
    private Boolean deletedByTo;

    @ApiModelProperty("消息发送时间（创建时间）")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("消息更新时间（更新时间）")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
