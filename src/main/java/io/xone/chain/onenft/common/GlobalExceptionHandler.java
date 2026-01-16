package io.xone.chain.onenft.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Global Exception Handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public Result<?> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return Result.failed(e.getErrorCode());
        }
        return Result.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> handle(Exception e) {
        e.printStackTrace();
        return Result.failed(e.getMessage());
    }
}