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
@TableName("nft_bundles")
@ApiModel(value = "NftBundles对象", description = "")
public class NftBundles implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("creatorId")
    private Integer creatorId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("coverImage")
    private String coverImage;

    @TableField("price")
    private String price;

    @TableField("originalPrice")
    private String originalPrice;

    @TableField("discountPercentage")
    private Integer discountPercentage;

    @TableField("chainId")
    private String chainId;

    @TableField("itemCount")
    private Integer itemCount;

    @TableField("status")
    private String status;

    @TableField("buyerId")
    private Integer buyerId;

    @TableField("saleTxHash")
    private String saleTxHash;

    @TableField("soldAt")
    private LocalDateTime soldAt;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
