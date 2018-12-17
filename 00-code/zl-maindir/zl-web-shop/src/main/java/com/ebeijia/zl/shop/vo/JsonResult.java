package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.constants.ResultState;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = -62401379125965102L;

    /*
     * 响应状态码，默认ResponseCode.SUCCESS
     */
    private int code = ResultState.OK;

    /*
     * 响应详情说明
     */
    private String message = "";

    /*
     * 响应详情数据
     */
    private T result;

    /*
     * 默认构造器
     */
    public JsonResult() {
        setCode(ResultState.OK);
        setMessage("ok");
    }


    public JsonResult(T t) {
        this.result = t;
        if (t == null) {
            setCode(ResultState.NOT_FOUND);
            return;
        }
    }

    /*
     * 状态码构造器
     */
    public JsonResult(int responseCode) {
        setCode(responseCode);
        setMessage("");
        setResult(null);
    }

    /*
     * 状态码 + 消息详情构造器
     */
    public JsonResult(int responseCode, String message) {
        setCode(responseCode);
        setMessage(message);
        setResult(null);
    }

    public JsonResult<T> setCode(int responseCode) {
        code = responseCode;
        return this;
    }

    public int getCode() {
        return code;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public T getResult() {
        return this.result;
    }

}
