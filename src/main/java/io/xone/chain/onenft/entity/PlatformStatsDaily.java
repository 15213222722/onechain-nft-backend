package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台每日统计快照
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Getter
@Setter
@TableName("platform_stats_daily")
@ApiModel(value = "PlatformStatsDaily对象", description = "平台每日统计快照")
public class PlatformStatsDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("统计日期 (YYYY-MM-DD)")
    @TableId("stat_date")
    private LocalDate statDate;

    @ApiModelProperty("所有 Sale payment_amount 之和")
    @TableField("trade_volume_raw")
    private Long tradeVolumeRaw;

    @ApiModelProperty("累计交易")
    @TableField("trade_count")
    private Integer tradeCount;

    @ApiModelProperty("DISTINCT taker")
    @TableField("unique_buyers")
    private Integer uniqueBuyers;

    @ApiModelProperty("DISTINCT lister")
    @TableField("unique_sellers")
    private Integer uniqueSellers;

    @ApiModelProperty("累计手续费")
    @TableField("fee_earned_raw")
    private Long feeEarnedRaw;

    @ApiModelProperty("累计swap单数")
    @TableField("swap_count")
    private Integer swapCount;

    @ApiModelProperty("累计sale单数")
    @TableField("sale_count")
    private Integer saleCount;

    @ApiModelProperty("当前活跃挂单数 (需定时更新)")
    @TableField("active_listings")
    private Integer activeListings;

    @ApiModelProperty("当天新增挂单")
    @TableField("created_listings")
    private Integer createdListings;

    @ApiModelProperty("当天取消挂单")
    @TableField("cancelled_listings")
    private Integer cancelledListings;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
