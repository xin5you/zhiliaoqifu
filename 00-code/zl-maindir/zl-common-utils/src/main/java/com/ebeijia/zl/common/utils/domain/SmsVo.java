package com.ebeijia.zl.common.utils.domain;

/**
 * 短信参数
 */
public class SmsVo implements  java.io.Serializable {

    /**
     * 消息主键
     */
    private String msgId;

    private String smsType; // com.ebeijia.zl.common.utils.enums.SMSType
    /**
     * 手机号
     */
    private String phoneNumber;

    /***
     * 企业名称
     */
    private String company;

    /**
     * 交易金额
     */
    private String amount;

    /**
     * 短信验证码
     */
    private String code;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
