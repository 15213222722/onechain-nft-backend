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
@TableName("nft_loans")
@ApiModel(value = "NftLoans对象", description = "")
public class NftLoans implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("borrowerId")
    private Integer borrowerId;

    @TableField("lenderId")
    private Integer lenderId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("collectionName")
    private String collectionName;

    @TableField("status")
    private String status;

    @TableField("principalAmount")
    private String principalAmount;

    @TableField("interestRate")
    private String interestRate;

    @TableField("durationDays")
    private Integer durationDays;

    @TableField("repaymentAmount")
    private String repaymentAmount;

    @TableField("startDate")
    private LocalDateTime startDate;

    @TableField("dueDate")
    private LocalDateTime dueDate;

    @TableField("repaidDate")
    private LocalDateTime repaidDate;

    @TableField("loanTxHash")
    private String loanTxHash;

    @TableField("repaymentTxHash")
    private String repaymentTxHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
