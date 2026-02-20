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
 * 收藏表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
@Getter
@Setter
@TableName("user_collections")
@ApiModel(value = "UserCollections对象", description = "收藏表")
public class UserCollections implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("wallet_address")
    private String walletAddress;

    @ApiModelProperty("nft对象id")
    @TableField("nft_object_id")
    private String nftObjectId;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
