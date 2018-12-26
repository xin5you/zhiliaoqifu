package com.ebeijia.zl.facade.account.enums;

public enum  WithDrawSuccessEnum {

    True("true"),
    Flase("flase"),
    Processing("processing");

    private String code;

    WithDrawSuccessEnum(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }
}
