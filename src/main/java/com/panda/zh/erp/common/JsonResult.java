package com.panda.zh.erp.common;


/**
 * @author Panda.Z
 */
@SuppressWarnings("unused")
public class JsonResult<T> {

    private int code;
    private String message;
    private T data;

    public JsonResult(int code) {
        this.code = code;
    }

    public JsonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == ErrorCode.Success.SUCCESS.getCode();
    }
}
