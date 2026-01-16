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
@TableName("bundle_items")
@ApiModel(value = "BundleItems对象", description = "")
public class BundleItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("bundleId")
    private Integer bundleId;

    @TableField("tokenId")
    private String tokenId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("nftName")
    private String nftName;

    @TableField("nftImage")
    private String nftImage;

    @TableField("individualPrice")
    private String individualPrice;

    @TableField("rarity")
    private String rarity;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
