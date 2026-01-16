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
@TableName("airdrop_recipients")
@ApiModel(value = "AirdropRecipients对象", description = "")
public class AirdropRecipients implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("airdropId")
    private Integer airdropId;

    @TableField("walletAddress")
    private String walletAddress;

    @TableField("userId")
    private Integer userId;

    @TableField("amount")
    private Integer amount;

    @TableField("claimed")
    private Boolean claimed;

    @TableField("claimTxHash")
    private String claimTxHash;

    @TableField("claimedAt")
    private LocalDateTime claimedAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
