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
@TableName("rental_history")
@ApiModel(value = "RentalHistory对象", description = "")
public class RentalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("rentalId")
    private Integer rentalId;

    @TableField("renterId")
    private Integer renterId;

    @TableField("ownerId")
    private Integer ownerId;

    @TableField("daysRented")
    private Integer daysRented;

    @TableField("totalPrice")
    private String totalPrice;

    @TableField("collateralAmount")
    private String collateralAmount;

    @TableField("collateralReturned")
    private Boolean collateralReturned;

    @TableField("startedAt")
    private LocalDateTime startedAt;

    @TableField("endedAt")
    private LocalDateTime endedAt;

    @TableField("txHash")
    private String txHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
