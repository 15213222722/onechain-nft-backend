package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.enums.CategoryEnum;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 集合验证申请请求体
 */
@ApiModel(value = "VerificationApplicationRequest", description = "集合验证申请请求体")
@Data
@Validated
public class VerificationApplicationRequest {
    @ApiModelProperty(value = "申请人钱包地址，格式0x...（64 hex）", required = true)
    @NotBlank(message = "申请人钱包不能为空")
    private String applicantWallet;

    @ApiModelProperty(value = "集合类型标识，Move类型如0x...::module::Name", required = true)
    @NotBlank(message = "集合类型不能为空")
    private String collectionType;

    @ApiModelProperty(value = "集合展示名称", required = true)
    @NotBlank(message = "集合名称不能为空")
    private String collectionName;

    @ApiModelProperty(value = "集合logo图片URL（可为CDN/IPFS）", required = true)
    @NotBlank(message = "Logo不能为空")
    private String logoUrl;

    @ApiModelProperty(value = "集合描述", required = true)
    @NotBlank(message = "集合描述不能为空")
    private String description;

    @ApiModelProperty(value = "官网或项目页面URL")
    private String websiteUrl;

    @ApiModelProperty(value = "社交链接JSON，如{\"twitter\":\"\", \"discord\":\"\"}")
    private String socialLinks;

    @ApiModelProperty(value = "唯一持有者数量（>=0）", required = true)
    @NotNull(message = "持有者数量不能为空")
    private Integer uniqueHoldersCount;

    @ApiModelProperty(value = "是否原创项目（申请者声明）", required = true)
    @NotNull(message = "是否原创不能为空")
    private Boolean isOriginal;

    @ApiModelProperty(value = "证明材料，可包含图片/文档URL列表或说明")
    private String proofMaterials;

    @ApiModelProperty(value = "联系人邮箱")
    @Email(message = "邮箱格式不正确")
    private String contactEmail;

    @ApiModelProperty(value = "是否同意条款（必选）", required = true)
    @NotNull(message = "请确认您已阅读并同意相关条款")
    private Boolean agreedTerms;
    
    @ApiModelProperty("是否符合display标准")
    private Boolean isDisplay;

    @ApiModelProperty(value = "分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER", required = true)
    @NotNull(message = "分类不能为空")
    private CategoryEnum categoryEnum;
}