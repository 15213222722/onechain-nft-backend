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
 * 用户表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-23
 */
@Getter
@Setter
@TableName("users")
@ApiModel(value = "Users对象", description = "用户表")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户名")
    @TableField("name")
    private String name;

    @ApiModelProperty("钱包地址")
    @TableField("walletAddress")
    private String walletAddress;

    @ApiModelProperty("capId")
    @TableField("capId")
    private String capId;

    @ApiModelProperty("市场id")
    @TableField("kioskId")
    private String kioskId;

    @ApiModelProperty("email")
    @TableField("email")
    private String email;

    @ApiModelProperty("上次登录时间")
    @TableField("lastSignedIn")
    private LocalDateTime lastSignedIn;

    @ApiModelProperty("头像")
    @TableField("avatarUrl")
    private String avatarUrl;

    @ApiModelProperty("个人简介")
    @TableField("description")
    private String description;

    @ApiModelProperty("twitter")
    @TableField("twitter")
    private String twitter;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
