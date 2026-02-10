package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityTypeEnum {
    NFT_MINTED("NFT_MINTED"),
    NFT_LISTED("NFT_LISTED"),
    NFT_UNLISTED("NFT_UNLISTED"),
    NFT_SOLD("NFT_SOLD"),
    NFT_BOUGHT("NFT_BOUGHT"),
    NFT_SWAPPED("NFT_SWAPPED"),
    USER_FOLLOWED("USER_FOLLOWED"),
    ITEM_COLLECTED("ITEM_COLLECTED"),
    ITEM_UNCOLLECTED("ITEM_UNCOLLECTED"),
    NFT_FAVORITED("NFT_FAVORITED"),
    NFT_UNFAVORITED("NFT_UNFAVORITED");

    private final String value;
}
