package io.xone.chain.onenft.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CollectionPageDTO {

	@ApiModelProperty("分类：PFP/ART/GAMING/COLLECTIBLE/METAVERSE/MUSIC/FASHION/DOMAIN/UTILITY/MEME/RWA/OTHER")
	private String category;

	@ApiModelProperty("集合展示名称")
	private String collectionName;

	private String collectionSlug;

	@ApiModelProperty("集合描述")
	private String description;

	@ApiModelProperty("NFT 数量")
	private long nftCount;

	@ApiModelProperty("持有者地址数")
	private long holderCount;

	@ApiModelProperty("集合 logo 图片 URL（可为 CDN/IPFS）")
	private String logoUrl;

	@ApiModelProperty("官网或项目页面 URL")
	private String websiteUrl;

	@ApiModelProperty("社交链接 JSON，如 {\"twitter\":\"\", \"discord\":\"\"}")
	private String socialLinks;

	@ApiModelProperty("是否原创项目（申请者声明）")
	private Boolean isOriginal;

	@ApiModelProperty("集合是否已验证")
	private Boolean verified;
}