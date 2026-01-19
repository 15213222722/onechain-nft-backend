package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RarityEnum {
    COMMON("common", "Common"),
    RARE("rare", "Rare"),
    EPIC("epic", "Epic"),
    LEGENDARY("legendary", "Legendary");

    private final String code;
    private final String description;

    public static RarityEnum getByCode(String code) {
        for (RarityEnum value : values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
