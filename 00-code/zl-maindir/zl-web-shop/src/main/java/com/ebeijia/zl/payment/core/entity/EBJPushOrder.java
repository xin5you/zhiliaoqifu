package com.ebeijia.zl.payment.core.entity;


import com.ebeijia.zl.payment.util.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * EBJ支付
 *
 * @author xiong.wang
 * @date 2017/7/14
 */
public class EBJPushOrder {
    private String merid;
    private String key;
    private String merchantOutOrderNo;
    private String noncestr;
    private String orderMoney;
    private String orderTime;


    public static class Builder {
        private String key;
        private String merid;
        private String merchantOutOrderNo;
        private String noncestr;
        private String orderMoney;
        private String orderTime;

        public Builder(String merid, String key) {
            this.merid = merid;
            this.key = key;
        }

        public Builder merchantOutOrderNo(String merchantOutOrderNo) {
            this.merchantOutOrderNo = merchantOutOrderNo;
            return this;
        }

        public Builder noncestr(String noncestr) {
            this.noncestr = noncestr;
            return this;
        }

        public Builder orderMoney(String orderMoney) {
            this.orderMoney = orderMoney;
            return this;
        }

        public Builder orderTime(String orderTime) {
            this.orderTime = orderTime;
            return this;
        }

        public EBJPushOrder build() {
            return new EBJPushOrder(this);
        }
    }

    public EBJPushOrder(){}

    private EBJPushOrder(Builder builder) {
        merchantOutOrderNo = builder.merchantOutOrderNo;
        merid = builder.merid;
        noncestr = builder.noncestr;
        orderMoney = builder.orderMoney;
        orderTime = builder.orderTime;
        key = builder.key;
    }


    public String getMerchantOutOrderNo() {
        return merchantOutOrderNo;
    }

    public void setMerchantOutOrderNo(String merchantOutOrderNo) {
        this.merchantOutOrderNo = merchantOutOrderNo;
    }

    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("merchantOutOrderNo=").append(this.merchantOutOrderNo);
        sb.append("&merid=").append(this.merid);
        sb.append("&noncestr=").append(this.noncestr);
        sb.append("&orderMoney=").append(this.orderMoney);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append("&orderTime=").append(sdf.format(new Date()));
        String sign = MD5.sign(sb.toString(), "&key=" + this.key, "utf-8");
        sb.append("&sign=" + sign);
        return sb.toString();
    }
}
