package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Listing query Search Request")
public class ListingQueryRequest extends PageRequest {
	
	private String walletAddress;

}
