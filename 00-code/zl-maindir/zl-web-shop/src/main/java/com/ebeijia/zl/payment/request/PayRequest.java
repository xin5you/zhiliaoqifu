package com.ebeijia.zl.payment.request;


import com.ebeijia.zl.payment.util.MD5;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 支付请求
 *
 * @author xiong.wang
 * @date 2017/6/19
 */
public class PayRequest implements Serializable {
    /*商户订单号*/
    private String merchantOutOrderNo;
    /*商户号*/
    private String merid;
    /*随机字符串*/
    private String noncestr;
    /*订单金额*/
    private String orderMoney;
    /*订单时间*/
    private String orderTime;
    /*商户密钥*/
    private String key;
    /*签名*/
    private String sign;
    /*支付渠道*/
    String channel;
    /*支付类型*/
    String payType;

    public PayRequest() {
    }

    public PayRequest(String merchantOutOrderNo, String merid, String noncestr, String orderMoney, String orderTime, String key, String sign, String channel, String payType) {
        this.merchantOutOrderNo = merchantOutOrderNo;
        this.merid = merid;
        this.noncestr = noncestr;
        this.orderMoney = orderMoney;
        this.orderTime = orderTime;
        this.key = key;
        this.sign = sign;
        this.channel = channel;
        this.payType = payType;
    }

    public PayRequest(PayRequest payRequest) {
        this.merchantOutOrderNo = payRequest.getMerchantOutOrderNo();
        this.merid = payRequest.getMerid();
        this.noncestr = payRequest.getNoncestr();
        this.orderMoney = payRequest.getOrderMoney();
        this.orderTime = payRequest.getOrderTime();
        this.key = payRequest.getKey();
        this.sign = payRequest.getSign();
        this.channel = payRequest.getChannel();
        this.payType = payRequest.getPayType();
    }

    /**
     * 构建对象
     */
    public static class Builder {

        private PayRequest target;

        public Builder() {
            target = new PayRequest();
        }

        public Builder merchantOutOrderNo(String merchantOutOrderNo) {
            target.merchantOutOrderNo = merchantOutOrderNo;
            return this;
        }

        public Builder merid(String merid) {
            target.merid = merid;
            return this;
        }

        public Builder noncestr(String noncestr) {
            target.noncestr = noncestr;
            return this;
        }

        public Builder orderMoney(String orderMoney) {
            target.orderMoney = orderMoney;
            return this;
        }

        public Builder orderTime(String orderTime) {
            target.orderTime = orderTime;
            return this;
        }

        public Builder key(String key) {
            target.key = key;
            return this;
        }

        public Builder sign(String sign) {
            target.sign = sign;
            return this;
        }

        public Builder channel(String channel) {
            target.channel = channel;
            return this;
        }

        public Builder payType(String payType) {
            target.payType = payType;
            return this;
        }

        public PayRequest build() {
            return new PayRequest(target);
        }

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("merchantOutOrderNo=").append(merchantOutOrderNo);
        sb.append("&merid=").append(merid);
        sb.append("&noncestr=").append(noncestr);
        sb.append("&orderMoney=").append(orderMoney);
        sb.append("&orderTime=").append(orderTime);
        String sign = MD5.sign(sb.toString(), "&key=" + key, "utf-8");
        sb.append("&sign=" + sign);
        return sb.toString();
    }

    public String queryToString() {
        StringBuffer sb = new StringBuffer();
        sb.append("merchantOutOrderNo=").append(merchantOutOrderNo);
        sb.append("&merid=").append(merid);
        sb.append("&noncestr=").append(noncestr);
        String sign = MD5.sign(sb.toString(), "&key=" + key, "utf-8");
        sb.append("&sign=" + sign);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String merchantId = "100100104";
        String merchantOutOrderNo = "test";
        String nonceStr = UUID.randomUUID().toString().trim()
                .replaceAll("-", "");
        String money = "0.01";
        String key = "123";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = sdf.format(new Date());
        PayRequest payRequest = new PayRequest
                .Builder().merchantOutOrderNo(merchantOutOrderNo)
                .merid(merchantId)
                .noncestr(nonceStr)
                .orderMoney(money)
                .orderTime(dateTime)
                .key(key).build();
        System.out.println(payRequest.toString());
    }
}
