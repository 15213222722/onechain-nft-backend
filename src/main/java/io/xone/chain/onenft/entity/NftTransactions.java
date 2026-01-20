package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * nft交易跟踪表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("nft_transactions")
@ApiModel(value = "NftTransactions对象", description = "nft交易跟踪表")
public class NftTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("交易类型,mint:铸造;list:上架;unlist:下架;transfer:转移;auction_won:竞拍")
    @TableField("type")
    private String type;

    @ApiModelProperty("交易用户id")
    @TableField("fromUserId")
    private Integer fromUserId;

    @ApiModelProperty("对方用户id")
    @TableField("toUserId")
    private Integer toUserId;

    @ApiModelProperty("成交价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("交易hash")
    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
