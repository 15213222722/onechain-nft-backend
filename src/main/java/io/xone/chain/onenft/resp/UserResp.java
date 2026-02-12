package io.xone.chain.onenft.resp;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "User Response", description = "User Information Response")
public class UserResp {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("用户名")
	private String name;

	@ApiModelProperty("钱包地址")
	private String walletAddress;

	@ApiModelProperty("email")
	private String email;

	@ApiModelProperty("上次登录时间")
	private LocalDateTime lastSignedIn;

	@ApiModelProperty("头像")
	private String avatarUrl;

	@ApiModelProperty("个人简介")
	private String description;

	@ApiModelProperty("twitter")
	private String twitter;
	
	@ApiModelProperty("UserStatsResp")
	private UserStatsResp stats;


	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
