package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("My kiosk NFT Search Request")
public class MyKioskNftRequest extends PageRequest {
	
	private String walletAddress;

}
