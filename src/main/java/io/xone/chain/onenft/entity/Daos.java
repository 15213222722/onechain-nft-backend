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
@TableName("daos")
@ApiModel(value = "Daos对象", description = "")
public class Daos implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("fractionId")
    private Integer fractionId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("quorumPercentage")
    private Integer quorumPercentage;

    @TableField("votingPeriodHours")
    private Integer votingPeriodHours;

    @TableField("proposalThreshold")
    private Integer proposalThreshold;

    @TableField("treasuryBalance")
    private String treasuryBalance;

    @TableField("status")
    private String status;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
