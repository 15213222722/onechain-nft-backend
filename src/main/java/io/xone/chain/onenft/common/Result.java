package io.xone.chain.onenft.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Common Result Object
 */
@Data
@ApiModel(value = "Common Result Object", description = "Common Result Object")
public class Result<T> {
    @ApiModelProperty(value = "Status Code", example = "200")
    private long code;
    @ApiModelProperty(value = "Message", example = "Operation success")
    private String message;
    @ApiModelProperty(value = "Data")
    private T data;

    protected Result() {
    }

    protected Result(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Success result
     *
     * @param data Fetched data
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    /**
     * Success result
     *
     * @param data Fetched data
     * @param  message Message
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<T>(ErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * Failed result
     * @param errorCode Error code
     */
    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * Failed result
     * @param errorCode Error code
     * @param message Error message
     */
    public static <T> Result<T> failed(IErrorCode errorCode, String message) {
        return new Result<T>(errorCode.getCode(), message, null);
    }

    /**
     * Failed result
     * @param message Message
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>(ErrorCode.FAILED.getCode(), message, null);
    }

    /**
     * Failed result
     */
    public static <T> Result<T> failed() {
        return failed(ErrorCode.FAILED);
    }
}