package io.xone.chain.onenft.common;

/**
 * Common API error codes
 */
public enum ErrorCode implements IErrorCode {
    SUCCESS(200, "Operation success"),
    FAILED(500, "Operation failed"),
    VALIDATE_FAILED(404, "Parameter validation failed"),
    UNAUTHORIZED(401, "Not logged in or token expired"),
    FORBIDDEN(403, "No relevant permission");

    private long code;
    private String message;

    private ErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}