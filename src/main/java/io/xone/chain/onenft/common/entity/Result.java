package io.xone.chain.onenft.common.entity;


import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uniform API Response Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "General API Response")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Status Code (200 for success)", example = "200")
    private long code;

    @ApiModelProperty(value = "Response Message", example = "Success")
    private String message;

    @ApiModelProperty(value = "Response Data")
    private T data;

    /**
     * Success with data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    /**
     * Success without data
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * Failed with message
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * Failed with code and message
     */
    public static <T> Result<T> failed(long code, String message) {
        return new Result<>(code, message, null);
    }
}

