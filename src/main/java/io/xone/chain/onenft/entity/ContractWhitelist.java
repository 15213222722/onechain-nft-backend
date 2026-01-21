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
 * 合约白名单表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-21
 */
@Getter
@Setter
@TableName("contract_whitelist")
@ApiModel(value = "ContractWhitelist对象", description = "合约白名单表")
public class ContractWhitelist implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("合约")
    @TableField("packageId")
    private String packageId;

    @ApiModelProperty("白名单名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("是否启用")
    @TableField("isActive")
    private Boolean isActive;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("网站")
    @TableField("websiteUrl")
    private String websiteUrl;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
