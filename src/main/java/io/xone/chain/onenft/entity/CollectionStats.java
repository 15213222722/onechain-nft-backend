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
 * NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等）
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Getter
@Setter
@TableName("collection_stats")
@ApiModel(value = "CollectionStats对象", description = "NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等）")
public class CollectionStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("NFT 集合的唯一标识符（slug），通常为链下定义的标准化名称（如 \"sui-punks\"、\"degod-sui\"）")
    @TableId("collection_slug")
    private String collectionSlug;

    @ApiModelProperty("统计周期：24h=过去24小时，7d=过去7天，30d=过去30天，all=累计全部时间")
    @TableId("period")
    private String period;

    @ApiModelProperty("该周期内该集合的总成交金额（仅 Sale 类型，以原始数值计，单位为代币最小单位）")
    @TableField("trade_volume_raw")
    private Long tradeVolumeRaw;

    @ApiModelProperty("该周期内该集合的总成交笔数（Sale + Swap 均计入）")
    @TableField("trade_count")
    private Integer tradeCount;

    @ApiModelProperty("该周期内参与该集合交易的独立用户数（DISTINCT taker 和 lister）")
    @TableField("unique_traders")
    private Integer uniqueTraders;

    @ApiModelProperty("当前最低活跃 SALE 挂单价格（原始数值，需根据 coin decimals 换算展示）")
    @TableField("floor_price_raw")
    private Long floorPriceRaw;

    @ApiModelProperty("最后一次更新地板价的时间（用于判断 floor_price_raw 是否新鲜）")
    @TableField("last_floor_update")
    private LocalDateTime lastFloorUpdate;

    @ApiModelProperty("该周期内 Sale 类型（固定价购买）的成交笔数")
    @TableField("sale_count")
    private Integer saleCount;

    @ApiModelProperty("该周期内 Swap 类型（NFT 交换）的成交笔数")
    @TableField("swap_count")
    private Integer swapCount;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
