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
@TableName("notifications")
@ApiModel(value = "Notifications对象", description = "")
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("type")
    private String type;

    @TableField("title")
    private String title;

    @TableField("message")
    private String message;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftImage")
    private String nftImage;

    @TableField("amount")
    private String amount;

    @TableField("relatedUserId")
    private Integer relatedUserId;

    @TableField("actionUrl")
    private String actionUrl;

    @TableField("isRead")
    private Boolean isRead;

    @TableField("metadata")
    private String metadata;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
