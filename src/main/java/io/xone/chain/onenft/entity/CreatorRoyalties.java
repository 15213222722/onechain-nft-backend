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
@TableName("creator_royalties")
@ApiModel(value = "CreatorRoyalties对象", description = "")
public class CreatorRoyalties implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("creatorId")
    private Integer creatorId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("collectionName")
    private String collectionName;

    @TableField("royaltyPercentage")
    private Integer royaltyPercentage;

    @TableField("recipientAddress")
    private String recipientAddress;

    @TableField("secondaryRecipientAddress")
    private String secondaryRecipientAddress;

    @TableField("secondaryPercentage")
    private Integer secondaryPercentage;

    @TableField("chainId")
    private String chainId;

    @TableField("enforced")
    private Boolean enforced;

    @TableField("totalEarned")
    private String totalEarned;

    @TableField("salesCount")
    private Integer salesCount;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
