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
@TableName("collections")
@ApiModel(value = "Collections对象", description = "")
public class Collections implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("coverImage")
    private String coverImage;

    @TableField("creatorId")
    private Integer creatorId;

    @TableField("contractAddress")
    private String contractAddress;

    @TableField("floorPrice")
    private String floorPrice;

    @TableField("totalVolume")
    private String totalVolume;

    @TableField("itemCount")
    private Integer itemCount;

    @TableField("ownerCount")
    private Integer ownerCount;

    @TableField("isVerified")
    private Boolean isVerified;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
