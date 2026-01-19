package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListingStatusEnum {
    LISTED(true, "Listed"),
    NOT_LISTED(false, "Not Listed");

    private final Boolean code;
    private final String description;
}
