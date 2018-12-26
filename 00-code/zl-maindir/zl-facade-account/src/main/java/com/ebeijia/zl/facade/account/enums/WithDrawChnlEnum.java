package com.ebeijia.zl.facade.account.enums;

public enum WithDrawChnlEnum {

    YFB("YFB"),   //苏宁-易付宝
    ALIPAY("ALIPAY"); //支付宝

    private String code;

    WithDrawChnlEnum(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }
}
