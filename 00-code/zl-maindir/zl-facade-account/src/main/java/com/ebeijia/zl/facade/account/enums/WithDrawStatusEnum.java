package com.ebeijia.zl.facade.account.enums;

public enum WithDrawStatusEnum {

    Status00("00","新建"),
    Status01("01","已受理"),
    Status02("02","处理中"),
    Status03("03","支付中"),
    Status04("04","支付失败"),
    Status05("05","支付成功"),
    Status06("06","支付异常"),
    Status07("07","处理成功");

    private String code;

    private String name;

    WithDrawStatusEnum(String code,String name){
        this.code=code;
        this.name=name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
