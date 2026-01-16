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
@TableName("votes")
@ApiModel(value = "Votes对象", description = "")
public class Votes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("proposalId")
    private Integer proposalId;

    @TableField("voterId")
    private Integer voterId;

    @TableField("choice")
    private String choice;

    @TableField("votingPower")
    private Integer votingPower;

    @TableField("reason")
    private String reason;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
