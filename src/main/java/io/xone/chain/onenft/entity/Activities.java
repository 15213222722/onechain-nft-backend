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
@TableName("activities")
@ApiModel(value = "Activities对象", description = "")
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("type")
    private String type;

    @TableField("userId")
    private Integer userId;

    @TableField("targetUserId")
    private Integer targetUserId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("price")
    private String price;

    @TableField("txHash")
    private String txHash;

    @TableField("metadata")
    private String metadata;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
