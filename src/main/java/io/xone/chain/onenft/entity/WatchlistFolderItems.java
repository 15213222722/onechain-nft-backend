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
@TableName("watchlist_folder_items")
@ApiModel(value = "WatchlistFolderItems对象", description = "")
public class WatchlistFolderItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("folderId")
    private Integer folderId;

    @TableField("watchlistItemId")
    private Integer watchlistItemId;

    @TableField("displayOrder")
    private Integer displayOrder;

    @TableField("createdAt")
    private LocalDateTime createdAt;
}
