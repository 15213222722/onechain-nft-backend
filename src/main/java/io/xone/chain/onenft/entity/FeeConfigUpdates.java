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
 * 手续费配置变更记录（FeeUpdated 事件对应的历史记录）
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Getter
@Setter
@TableName("fee_config_updates")
@ApiModel(value = "FeeConfigUpdates对象", description = "手续费配置变更记录（FeeUpdated 事件对应的历史记录）")
public class FeeConfigUpdates implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("手续费配置对象的 Sui Object ID (0x...，对应 FeeConfig 的 id)")
    @TableField("fee_config_object_id")
    private String feeConfigObjectId;

    @ApiModelProperty("变更前的费率（basis points，例如 100 = 1%）")
    @TableField("old_fee_bps")
    private Integer oldFeeBps;

    @ApiModelProperty("变更后的费率（basis points，例如 250 = 2.5%）")
    @TableField("new_fee_bps")
    private Integer newFeeBps;

    @ApiModelProperty("变更前的手续费接收地址")
    @TableField("old_recipient")
    private String oldRecipient;

    @ApiModelProperty("变更后的手续费接收地址")
    @TableField("new_recipient")
    private String newRecipient;

    @ApiModelProperty("执行费率变更的 Sui 交易 Digest（交易哈希/标识符）")
    @TableField("tx_digest")
    private String txDigest;

    @ApiModelProperty("费率变更发生的链上时间戳（毫秒，来自 tx_context::epoch_timestamp_ms）")
    @TableField("timestamp_ms")
    private Long timestampMs;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
