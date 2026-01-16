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
@TableName("airdrops")
@ApiModel(value = "Airdrops对象", description = "")
public class Airdrops implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("creatorId")
    private Integer creatorId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("collectionId")
    private String collectionId;

    @TableField("nftId")
    private String nftId;

    @TableField("fractionId")
    private Integer fractionId;

    @TableField("totalAmount")
    private Integer totalAmount;

    @TableField("claimedAmount")
    private Integer claimedAmount;

    @TableField("airdropType")
    private String airdropType;

    @TableField("status")
    private String status;

    @TableField("startAt")
    private LocalDateTime startAt;

    @TableField("endAt")
    private LocalDateTime endAt;

    @TableField("chainId")
    private String chainId;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
