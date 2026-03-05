package io.xone.chain.onenft.enums;

/**
 * 集合验证申请状态枚举
 */
public enum VerificationStatusEnum {
    PENDING("pending", "待审核"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝"),
    REVOKED("revoked", "已撤销");

    private final String code;
    private final String desc;

    VerificationStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
