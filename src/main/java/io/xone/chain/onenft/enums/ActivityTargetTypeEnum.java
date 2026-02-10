package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityTargetTypeEnum {

	NFT("NFT"), 
	LISTING("LISTING"), 
	USER("USER");

	private final String value;
}
