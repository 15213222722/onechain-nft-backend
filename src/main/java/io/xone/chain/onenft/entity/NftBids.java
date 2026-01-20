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
 * 竞价记录跟踪表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("nft_bids")
@ApiModel(value = "NftBids对象", description = "竞价记录跟踪表")
public class NftBids implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("竞价用户id")
    @TableField("bidderUserId")
    private Integer bidderUserId;

    @ApiModelProperty("金额")
    @TableField("amount")
    private String amount;

    @ApiModelProperty("是否胜出")
    @TableField("isWinning")
    private Boolean isWinning;

    @ApiModelProperty("hash")
    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
