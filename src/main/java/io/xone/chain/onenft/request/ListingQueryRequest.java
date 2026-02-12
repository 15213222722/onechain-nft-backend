package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Listing query Search Request")
public class ListingQueryRequest extends PageRequest {
	
	private String walletAddress;
	
	private String searchKey;
	
	private String sort; // price_asc, price_desc, time_asc, time_desc
}