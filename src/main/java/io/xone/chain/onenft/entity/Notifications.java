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
 * 系统通知表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("notifications")
@ApiModel(value = "Notifications对象", description = "系统通知表")
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("通知类型,bid_received:有人出价竞拍你的 NFT;bid_outbid:你被超越出价;auction_ending:拍卖即将结束;auction_won:你赢得了拍卖;auction_sold:你的拍卖已售出;offer_received:有人出价;offer_received:你的出价已被接受;sale_complete:你的 NFT 已售出;follow_new:有人关注了你;price_drop:关注的 NFT 价格下降;system:系统通知")
    @TableField("type")
    private String type;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("消息内容")
    @TableField("message")
    private String message;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("关联跳转地址")
    @TableField("actionUrl")
    private String actionUrl;

    @ApiModelProperty("是否已读")
    @TableField("isRead")
    private Boolean isRead;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
