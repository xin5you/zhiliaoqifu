package com.ebeijia.zl.shop.constants;

public interface OrderState {
    /**
     * 下单初始状态
     */
    int PRE_ORDER = 0;

    /**
     * 支付中、未收到支付详情
     */
    int WAIT_FOR_PAYMENT = 1;

    /**
     * 支付确认，收到支付详情
     */
    int PAYMENT_CONFIRM = 2;

    /**
     * 待发货
     */
    int WAIT_FOR_SHIP = 3;

    /**
     * 待确认收货
     */
    int WAIT_FOR_SHIP_CONFIRM = 4;

    /**
     * 待评价
     */
    int WAIT_FOR_COMMENT = 5;

    /**
     * 追加评论
     */
    int WAIT_FOR_COMMENT_ADDITIONAL = 6;

    /**
     * 订单已经完成，无法评价
     */
    int FINISHED = 7;

    /**
     * 进入售后状态（售后详情于Refund模块处理）
     */
    int RETURN = 8;

    /**
     * 订单失效，取消、关闭状态
     */
    int CLOSED = 9;

}
