package io.xone.chain.onenft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    PC("PC", "Computer"),
    ANDROID("ANDROID", "Android Device"),
    IOS("IOS", "iOS Device"),
    H5("H5", "Mobile Web");

    private final String code;
    private final String description;

    public static LoginTypeEnum getByCode(String code) {
        for (LoginTypeEnum value : values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}