package io.xone.chain.onenft.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
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
@TableName("users")
@ApiModel(value = "Users对象", description = "")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("openId")
    private String openId;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("loginMethod")
    private String loginMethod;

    @TableField("role")
    private String role;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

    @TableField("lastSignedIn")
    private LocalDateTime lastSignedIn;

    @TableField("avatarUrl")
    private String avatarUrl;

    @TableField("bio")
    private String bio;

    @TableField("walletAddress")
    private String walletAddress;
}
