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
 * 已处理事件表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Getter
@Setter
@TableName("processed_events")
@ApiModel(value = "ProcessedEvent对象", description = "已处理事件表")
public class ProcessedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("hash")
    @TableField("txHash")
    private String txHash;

    @ApiModelProperty("事件类型")
    @TableField("eventType")
    private String eventType;

    @TableField("processedAt")
    private LocalDateTime processedAt;
}
