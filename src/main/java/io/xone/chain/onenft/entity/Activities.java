package io.xone.chain.onenft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 活动跟踪表
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Getter
@Setter
@TableName("activities")
@ApiModel(value = "Activities对象", description = "活动跟踪表")
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("活动类型,mint:铸造;list:上架;sale:出售;transfer:转移;offer:报价;like:喜欢;follow:关注")
    @TableField("type")
    private String type;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("对方用户id")
    @TableField("targetUserId")
    private Integer targetUserId;

    @ApiModelProperty("nft对象id")
    @TableField("nftObjectId")
    private String nftObjectId;

    @ApiModelProperty("合约地址")
    @TableField("contractAddress")
    private String contractAddress;

    @ApiModelProperty("nft名称")
    @TableField("nftName")
    private String nftName;

    @ApiModelProperty("nft图片")
    @TableField("nftImage")
    private String nftImage;

    @ApiModelProperty("nft价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("hash")
    @TableField("txHash")
    private String txHash;

    @ApiModelProperty("额外信息")
    @TableField("metadata")
    private String metadata;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
