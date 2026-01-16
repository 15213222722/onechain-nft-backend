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
@TableName("nft_rentals")
@ApiModel(value = "NftRentals对象", description = "")
public class NftRentals implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("ownerId")
    private Integer ownerId;

    @TableField("renterId")
    private Integer renterId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("chainId")
    private String chainId;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("dailyPrice")
    private String dailyPrice;

    @TableField("minDays")
    private Integer minDays;

    @TableField("maxDays")
    private Integer maxDays;

    @TableField("collateralAmount")
    private String collateralAmount;

    @TableField("rentalStartAt")
    private LocalDateTime rentalStartAt;

    @TableField("rentalEndAt")
    private LocalDateTime rentalEndAt;

    @TableField("status")
    private String status;

    @TableField("totalEarnings")
    private String totalEarnings;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
