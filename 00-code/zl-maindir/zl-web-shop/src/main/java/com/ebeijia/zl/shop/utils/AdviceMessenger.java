package com.ebeijia.zl.shop.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 信使类
 * 利用异常管道传递讯息到前端
 *
 * 由于继承了RuntimeException，可能会导致事务回滚。
 * 请使用ShopTransactional注解替代Transactional
 */
@Data
public class AdviceMessenger extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 信息
     */
    protected String msg;

    /**
     * 状态码
     */
    protected int code;


    public AdviceMessenger(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public AdviceMessenger(String message) {
        this(200,message);
    }

}
