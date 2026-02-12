package io.xone.chain.onenft.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "User Stats Response", description = "User Stats Information Response")
public class UserStatsResp {
	
	private int collectedCount;
	
	private int createdCount;
	
	private int followersCount;
	
	private int followingCount;
	
	private long totalVolume;;

}
