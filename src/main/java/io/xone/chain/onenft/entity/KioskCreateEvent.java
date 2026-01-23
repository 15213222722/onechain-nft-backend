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
 * kiosk创建
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-23
 */
@Getter
@Setter
@TableName("kiosk_create_event")
@ApiModel(value = "KioskCreateEvent对象", description = "kiosk创建")
public class KioskCreateEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("钱包地址")
    @TableField("walletAddress")
    private String walletAddress;

    @ApiModelProperty("市场id")
    @TableField("kioskId")
    private String kioskId;

    @ApiModelProperty("hash")
    @TableField("txHash")
    private String txHash;

    @ApiModelProperty("事件type")
    @TableField("eventType")
    private String eventType;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
