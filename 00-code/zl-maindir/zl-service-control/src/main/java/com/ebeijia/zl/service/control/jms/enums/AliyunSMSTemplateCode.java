package com.ebeijia.zl.service.control.jms.enums;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;

public enum  AliyunSMSTemplateCode {

    SMSType1000("1000","SMS_154592195"),  // 知了企服注册验证码
    SMSType1001("1001","SMS_144452171"),  //知了企服密码重置短信验证码
    SMSType1002("1002","SMS_144452197"),  //知了企服充值短信通知
    SMSType1003("1003","SMS_144795029"),  //知了企服卡券转让验证码
    SMSType1004("1004","SMS_144452161"), //知了企服添加银行卡验证码
    SMSType1005("1005","SMS_153330915"); //安全验证码

    private String code;
    private String aliCode;

    AliyunSMSTemplateCode(String code, String aliCode) {
        this.code = code;
        this.aliCode = aliCode;
    }

    public String getCode() {
        return code;
    }

    public String getAliCode() {
        return aliCode;
    }

    public static AliyunSMSTemplateCode findByCode(String code) {
        for (AliyunSMSTemplateCode t : AliyunSMSTemplateCode.values()) {
            if (t.code.equalsIgnoreCase(code)) {
                return t;
            }
        }
        return null;
    }
}
