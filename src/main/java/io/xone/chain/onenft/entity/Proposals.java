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
@TableName("proposals")
@ApiModel(value = "Proposals对象", description = "")
public class Proposals implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("daoId")
    private Integer daoId;

    @TableField("proposerId")
    private Integer proposerId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("proposalType")
    private String proposalType;

    @TableField("executionData")
    private String executionData;

    @TableField("forVotes")
    private Integer forVotes;

    @TableField("againstVotes")
    private Integer againstVotes;

    @TableField("abstainVotes")
    private Integer abstainVotes;

    @TableField("status")
    private String status;

    @TableField("votingStartsAt")
    private LocalDateTime votingStartsAt;

    @TableField("votingEndsAt")
    private LocalDateTime votingEndsAt;

    @TableField("executedAt")
    private LocalDateTime executedAt;

    @TableField("executionTxHash")
    private String executionTxHash;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
