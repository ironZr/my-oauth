package com.zr.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 **/
@Setter
@Getter
public class PlatformException extends RuntimeException {

    private Object data;

    private Integer code = 500;

    private String msg;

    public PlatformException(String errorStr) {
        super(errorStr);
        msg = errorStr;
    }

    public PlatformException(Object data, String errorStr) {
        super(errorStr);
        msg = errorStr;
        this.data = data;
    }

    public PlatformException(Object data, Integer code, String errorStr) {
        super(errorStr);
        msg = errorStr;
        this.code = code;
        this.data = data;
    }

}