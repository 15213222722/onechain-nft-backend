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
 * NFT 集合验证申请记录表，保存申请者提交的材料与状态
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-05
 */
@Getter
@Setter
@TableName("verification_applications")
@ApiModel(value = "VerificationApplications对象", description = "NFT 集合验证申请记录表，保存申请者提交的材料与状态")
public class VerificationApplications implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("申请者钱包地址，格式 0x...（64 hex）")
    @TableField("applicant_wallet")
    private String applicantWallet;

    @ApiModelProperty("集合类型标识，Move 类型如 0x...::module::Name")
    @TableField("collection_type")
    private String collectionType;

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

    @ApiModelProperty("申请状态，例如 pending/approved/rejected")
    @TableField("status")
    private String status;

    @ApiModelProperty("若被拒绝，填写拒绝原因")
    @TableField("rejection_reason")
    private String rejectionReason;

    @ApiModelProperty("审核时间")
    @TableField("reviewed_at")
    private LocalDateTime reviewedAt;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @ApiModelProperty("是否符合display标准")
    @TableField("is_display")
    private Boolean isDisplay;

    @ApiModelProperty("分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER")
    @TableField("category")
    private String category;
}