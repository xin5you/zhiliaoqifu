package com.ebeijia.zl.facade.account.enums;

public enum WithDrawReceiverTypeEnum {

    PERSON("PERSON"),  //个人
    CORP("CORP");       //企业

    private String code;

    WithDrawReceiverTypeEnum(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }
}
