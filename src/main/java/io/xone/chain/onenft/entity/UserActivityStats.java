package io.xone.chain.onenft.entity;

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
 * 用户交易活动汇总表（用于用户排行榜、个人交易面板、鲸鱼榜等）
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@Getter
@Setter
@TableName("user_activity_stats")
@ApiModel(value = "UserActivityStats对象", description = "用户交易活动汇总表（用于用户排行榜、个人交易面板、鲸鱼榜等）")
public class UserActivityStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户钱包地址（Sui 地址，0x...，作为主键）")
    @TableId("address")
    private String address;

    @ApiModelProperty("该用户累计参与的成交次数（Sale + Swap 均计入）")
    @TableField("total_trades")
    private Integer totalTrades;

    @ApiModelProperty("该用户累计成交金额总和（仅 Sale 类型，以原始数值计，单位为代币最小单位）")
    @TableField("total_volume_raw")
    private Long totalVolumeRaw;

    @ApiModelProperty("该用户作为买家（taker）的累计成交金额（Sale 类型，支付金额）")
    @TableField("buy_volume_raw")
    private Long buyVolumeRaw;

    @ApiModelProperty("该用户作为卖家（lister）的累计成交金额（Sale 类型，收到金额，不含手续费）")
    @TableField("sell_volume_raw")
    private Long sellVolumeRaw;

    @ApiModelProperty("该用户累计支付的手续费总额（Sale 类型中作为买家支付的部分）")
    @TableField("fee_paid_raw")
    private Long feePaidRaw;

    @ApiModelProperty("该用户最后一次参与交易（成交）的时间（后端记录，基于链上 timestamp_ms）")
    @TableField("last_active")
    private LocalDateTime lastActive;

    @ApiModelProperty("按 total_volume_raw 降序排名的名次（可通过定时任务计算并更新）")
    @TableField("rank_volume")
    private Integer rankVolume;

    @ApiModelProperty("按 total_trades 降序排名的名次（可通过定时任务计算并更新）")
    @TableField("rank_trades")
    private Integer rankTrades;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
