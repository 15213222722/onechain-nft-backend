package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("User Activity Query Request")
public class UserActivityQueryRequest extends PageRequest {
	
	private String walletAddress; // Filter by specific user if needed
    private String activityType; // Filter by specific type
}
