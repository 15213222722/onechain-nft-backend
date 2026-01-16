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
@TableName("loan_offers")
@ApiModel(value = "LoanOffers对象", description = "")
public class LoanOffers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("loanId")
    private Integer loanId;

    @TableField("lenderId")
    private Integer lenderId;

    @TableField("offeredAmount")
    private String offeredAmount;

    @TableField("offeredInterestRate")
    private String offeredInterestRate;

    @TableField("offeredDurationDays")
    private Integer offeredDurationDays;

    @TableField("status")
    private String status;

    @TableField("expiresAt")
    private LocalDateTime expiresAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
