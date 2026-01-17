package io.xone.chain.onenft.common;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        return getBindResult(e.getBindingResult());
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<?> handleBindException(BindException e) {
        return getBindResult(e.getBindingResult());
    }

    private Result<?> getBindResult(BindingResult bindingResult) {
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + " " + fieldError.getDefaultMessage();
            }
        }
        return Result.failed(ErrorCode.VALIDATE_FAILED, message);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<?> handle(Exception e) {
        e.printStackTrace();
        return Result.failed(e.getMessage());
    }
}