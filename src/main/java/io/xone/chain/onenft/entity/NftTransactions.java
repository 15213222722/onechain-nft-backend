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
@TableName("nft_transactions")
@ApiModel(value = "NftTransactions对象", description = "")
public class NftTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("nftId")
    private Integer nftId;

    @TableField("type")
    private String type;

    @TableField("fromUserId")
    private Integer fromUserId;

    @TableField("toUserId")
    private Integer toUserId;

    @TableField("price")
    private String price;

    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
