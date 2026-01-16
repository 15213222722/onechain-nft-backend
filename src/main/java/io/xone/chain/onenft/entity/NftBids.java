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
@TableName("nft_bids")
@ApiModel(value = "NftBids对象", description = "")
public class NftBids implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("nftId")
    private Integer nftId;

    @TableField("bidderId")
    private Integer bidderId;

    @TableField("amount")
    private String amount;

    @TableField("isWinning")
    private Boolean isWinning;

    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
