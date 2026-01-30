package io.xone.chain.onenft.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("My Listing NFT Search Request")
public class MyListingNftRequest extends PageRequest {
	
	@NotBlank(message = "Wallet address cannot be blank")
	private String walletAddress;

}
