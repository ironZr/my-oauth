package com.zr.common;

import java.io.Serializable;

/**
 * 返回结果
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 123456789L;

    public static final int SUCCESS_CODE = 200;// 成功

    public static final int ERROR_CODE = 500;// 未知错误


    private static <T> R<T> getResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    /**
     * 结果体
     */
    protected T data;

    /**
     * 状态码
     */
    protected int code;

    /**
     * 信息
     */
    protected String msg;

    private R() {
        super();
    }

    public static <T> R<T> create(int code, String msg) {
        return getResult(null, code, msg);
    }

    public static final <T> R<T> ok() {
        return getResult(null, SUCCESS_CODE, null);
    }

    public static <T> R<T> ok(T data) {
        return getResult(data, SUCCESS_CODE, null);
    }

    public static <T> R<T> ok(int code, T data) {
        return getResult(data, code, null);
    }

    public static <T> R<T> ok(int code, T data, String msg) {
        return getResult(data, code, msg);
    }

    public static <T> R<T> failed(String msg) {
        return getResult(null, ERROR_CODE, msg);
    }

    public static <T> R<T>  failed(T data, String msg) {
        return getResult(data, ERROR_CODE, msg);
    }

    public static <T> R<T>  failed(T data, Integer code, String msg) {
        return getResult(data, code, msg);
    }

    //前端组件没适配code 不能用
    public static <T> R<T>  redirect(T data) {
        return getResult(data, 302, "重定向");
    }

    public static <T> R<T>  redirectOK(T data) {
        return getResult(data, SUCCESS_CODE, "重定向");
    }

    public T getData() {
        return data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public R<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public R<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
