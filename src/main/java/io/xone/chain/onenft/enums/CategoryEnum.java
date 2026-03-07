package io.xone.chain.onenft.enums;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "CategoryEnum", description = "NFT集合分类枚举")
public enum CategoryEnum {
	// 头像 / 身份类（最主流）
	PFP("Profile Picture", "头像 / 数字身份", "PFP 系列，常用于社交头像和会员权益"),

	// 艺术类
	ART("Digital Art", "数字艺术", "纯艺术品、生成艺术、1/1 或限量版"),

	// 游戏 / GameFi
	GAMING("Gaming", "游戏资产", "游戏内角色、道具、土地"),

	// 收藏品
	COLLECTIBLE("Collectible", "收藏品", "纪念品、限量版、体育/明星卡牌"),

	// 元宇宙 / 虚拟世界
	METAVERSE("Metaverse", "元宇宙", "虚拟土地、房产、空间"),

	// 音乐 / 音频
	MUSIC("Music", "音乐 / 音频", "音乐版权、专辑、演唱会门票"),

	// 时尚 / 可穿戴
	FASHION("Fashion", "数字时尚", "虚拟服装、配件、AR 穿戴"),

	// 摄影
	PHOTOGRAPHY("Photography", "摄影作品", "数字摄影师限量版"),

	// 域名 / 身份
	DOMAIN("Domain Name", "区块链域名", ".eth / .sui 等 Web3 身份"),

	// 实用 / 会员 / 门票
	UTILITY("Utility", "实用型 / 会员", "会员卡、门票、实物兑换凭证、DAO 权益"),

	// 迷因 / 社区驱动
	MEME("Meme", "迷因", "基于 meme 文化的搞笑或社区驱动系列"),

	// 现实资产 / 分片
	RWA("Real World Asset", "现实资产", "房产、艺术品、奢侈品的分片所有权"),

	// 其他 / 未分类
	OTHER("Other", "其他", "未归类或混合类型"),;

	private final String englishName;
	private final String chineseName;
	private final String description;

	CategoryEnum(String englishName, String chineseName, String description) {
		this.englishName = englishName;
		this.chineseName = chineseName;
		this.description = description;
	}

	public String getEnglishName() {
		return englishName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public String getDescription() {
		return description;
	}

	// 可选：根据英文名反查枚举（前端下拉用）
	public static CategoryEnum fromEnglishName(String name) {
		for (CategoryEnum c : values()) {
			if (c.englishName.equalsIgnoreCase(name)) {
				return c;
			}
		}
		return OTHER;
	}
}
