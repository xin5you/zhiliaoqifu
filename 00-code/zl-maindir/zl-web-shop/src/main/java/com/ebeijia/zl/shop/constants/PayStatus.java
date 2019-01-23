package com.ebeijia.zl.shop.constants;

public interface PayStatus {

    /**
     * 支付中、未收到支付详情
     */
    String WAIT_PAYMENT = "0";

    /**
     * 支付失败
     */
    String PAYMENT_FAIL = "1";

    /**
     * 支付确认，收到支付详情
     */
    String PAYMENT_CONFIRM = "2";

    /**
     * 进入售后状态（售后详情于Refund模块处理）
     */
    String RETURN = "8";

    /**
     * 退款完成
     */
    String CLOSED = "9";
}
