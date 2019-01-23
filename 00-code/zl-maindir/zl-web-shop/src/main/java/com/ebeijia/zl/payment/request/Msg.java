package com.ebeijia.zl.payment.request;

/**
 * 通知消息体
 * @author xiong.wang
 * @date 2017/6/19
 */
public class Msg {
    private String tradeNo;
    private String thirdNo;
    private String payMoney;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getThirdNo() {
        return thirdNo;
    }

    public void setThirdNo(String thirdNo) {
        this.thirdNo = thirdNo;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}
