package com.zr.exception;

import com.zr.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 全局异常处理
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
        自定义业务异常
     */

    @ExceptionHandler(PlatformException.class)
    public R platformExceptionHandle(PlatformException e) {
        if (null != e.getCause()) {
            log.error("抛出错误：", e);
            return R.failed(null, e.getCause().getMessage());
        }
        log.error("自定义业务异常 code:[{}] msg:[{}] data:[{}]", e.getCode(), e.getMessage(), e.getData());
        return R.failed(e.getData(), e.getCode(), e.getMsg());
    }

    /*
        请求方式错误
     */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R httpRequestMethodNotSupportedExceptionHandle(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方式错误 : {}", e.getMessage());
        return R.failed(null, e.getMessage());
    }

    /*
        请求参数有误 json
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SuppressWarnings("all")
    public R methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.warn("请求参数有误:{}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errStr = new StringBuilder();
        errStr.append("请求参数有误:\n");
        fieldErrors.parallelStream().forEach(tar -> errStr.append("[" + tar.getField() + ":" + tar.getDefaultMessage() + "]\n"));
        return R.failed(null, errStr.toString());
    }

    /*
        请求参数有误 form
     */

    @ExceptionHandler(BindException.class)
    @SuppressWarnings("all")
    public R bindExceptionHandler(BindException e) {
        log.warn("请求参数有误:{}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errStr = new StringBuilder();
        errStr.append("请求参数有误:\n");
        fieldErrors.parallelStream().forEach(tar -> errStr.append("[" + tar.getField() + ":" + tar.getDefaultMessage() + "]\n"));
        return R.failed(null, errStr.toString());
    }

    /*
        请求参数有误 @NotBlank @NotNull @NotEmpty
     */

    @ExceptionHandler(ConstraintViolationException.class)
    public R methodArgumentNotValidExceptionHandle(ConstraintViolationException e) {
        log.warn("请求参数有误:{}", e.getMessage());
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder errStr = new StringBuilder();
        errStr.append("请求参数有误:\n");
        constraintViolations.parallelStream().forEach(tar -> errStr.append(tar.getPropertyPath().toString()).append(Optional.ofNullable(tar.getMessage()).orElse("不能为空") + "\n"));
        return R.failed(null, errStr.toString());
    }

    /*
        未识别异常
     */
    @ExceptionHandler(Exception.class)
    public R exceptionHandle(Exception e) {
        log.error("未识别异常：", e);
        return R.failed("内部错误", e.getMessage());
    }


    /*
        类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R mismatchErrorHandler(MethodArgumentTypeMismatchException e) {
        String errStr = String.format("参数转换失败，方法：" + Objects.requireNonNull(e.getParameter().getMethod()).getName() + ",参数：" +
                e.getName() + "，信息：" + e.getLocalizedMessage());
        log.error(errStr);
        return R.failed(null, errStr);
    }

    /**
     * 媒体类型不支持
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R notSupportedType(HttpMediaTypeNotSupportedException e) {
        log.error("Content type不支持", e);
        return R.failed(null, Optional.ofNullable(e.getMessage()).orElse("Content type不支持"));
    }


}
