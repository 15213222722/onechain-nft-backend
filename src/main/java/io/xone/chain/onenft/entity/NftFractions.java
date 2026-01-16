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
@TableName("nft_fractions")
@ApiModel(value = "NftFractions对象", description = "")
public class NftFractions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("nftId")
    private Integer nftId;

    @TableField("ownerId")
    private Integer ownerId;

    @TableField("totalFractions")
    private Integer totalFractions;

    @TableField("availableFractions")
    private Integer availableFractions;

    @TableField("pricePerFraction")
    private String pricePerFraction;

    @TableField("tokenSymbol")
    private String tokenSymbol;

    @TableField("tokenName")
    private String tokenName;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("status")
    private String status;

    @TableField("buyoutPrice")
    private String buyoutPrice;

    @TableField("buyoutThreshold")
    private Integer buyoutThreshold;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
