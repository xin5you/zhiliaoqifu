package com.ebeijia.zl.shop.vo;

public class JsonResult<T> {

    private static final long serialVersionUID = -62401379125965102L;

    /*
     * 响应状态码，默认ResponseCode.SUCCESS
     */
    private int code = 200;

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
        setCode(200);
        setMessage("ok");
    }


    public JsonResult(T t){
        this.result = t;
        if (t == null){
            setCode(404);
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

    public void setCode(int responseCode) {
        code = responseCode;
    }

    public int getCode() {
       return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

}
