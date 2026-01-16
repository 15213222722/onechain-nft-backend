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
@TableName("shopping_cart")
@ApiModel(value = "ShoppingCart对象", description = "")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("collectionName")
    private String collectionName;

    @TableField("price")
    private String price;

    @TableField("sellerAddress")
    private String sellerAddress;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
