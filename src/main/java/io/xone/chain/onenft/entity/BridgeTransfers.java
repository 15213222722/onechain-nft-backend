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
@TableName("bridge_transfers")
@ApiModel(value = "BridgeTransfers对象", description = "")
public class BridgeTransfers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("nftId")
    private String nftId;

    @TableField("sourceChain")
    private String sourceChain;

    @TableField("destChain")
    private String destChain;

    @TableField("sourceTxHash")
    private String sourceTxHash;

    @TableField("destTxHash")
    private String destTxHash;

    @TableField("bridgeFee")
    private String bridgeFee;

    @TableField("status")
    private String status;

    @TableField("errorMessage")
    private String errorMessage;

    @TableField("estimatedCompletionAt")
    private LocalDateTime estimatedCompletionAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
