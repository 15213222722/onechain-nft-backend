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
 * 集合验证状态表，记录每个集合的验证结果与历史关联申请
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-06
 */
@Getter
@Setter
@TableName("collections_verification")
@ApiModel(value = "CollectionsVerification对象", description = "集合验证状态表，记录每个集合的验证结果与历史关联申请")
public class CollectionsVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("集合类型标识，唯一（如 Move type）")
    @TableField("collection_type")
    private String collectionType;

    @ApiModelProperty("集合是否已验证")
    @TableField("is_verified")
    private Boolean isVerified;

    @ApiModelProperty("验证通过时间")
    @TableField("verified_at")
    private LocalDateTime verifiedAt;

    @ApiModelProperty("最近一次关联的申请记录 ID")
    @TableField("last_application_id")
    private Long lastApplicationId;

    @ApiModelProperty("若被撤销，记录撤销时间")
    @TableField("revoked_at")
    private LocalDateTime revokedAt;

    @ApiModelProperty("撤销原因说明")
    @TableField("revoke_reason")
    private String revokeReason;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @ApiModelProperty("分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER")
    @TableField("category")
    private String category;

    @ApiModelProperty("集合展示名称")
    @TableField("collection_name")
    private String collectionName;

    @ApiModelProperty("集合 logo 图片 URL（可为 CDN/IPFS）")
    @TableField("logo_url")
    private String logoUrl;

    @ApiModelProperty("集合描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("官网或项目页面 URL")
    @TableField("website_url")
    private String websiteUrl;

    @ApiModelProperty("社交链接 JSON，如 {\"twitter\":\"\", \"discord\":\"\"}")
    @TableField("social_links")
    private String socialLinks;

    @ApiModelProperty("唯一持有者数量（>=0）")
    @TableField("unique_holders_count")
    private Integer uniqueHoldersCount;

    @ApiModelProperty("是否原创项目（申请者声明）")
    @TableField("is_original")
    private Boolean isOriginal;

    @ApiModelProperty("证明材料，可包含图片/文档 URL 列表或说明")
    @TableField("proof_materials")
    private String proofMaterials;

    @ApiModelProperty("联系人邮箱")
    @TableField("contact_email")
    private String contactEmail;

    @ApiModelProperty("是否同意条款（必选）")
    @TableField("agreed_terms")
    private Boolean agreedTerms;
}
