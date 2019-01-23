package com.ebeijia.zl.payment.request;


import com.ebeijia.zl.payment.util.MD5;

import java.io.Serializable;

/**
 * 通知请求
 *
 * @author xiong.wang
 * @date 2017/6/19
 */
public class NoticeRequest implements Serializable {
    /*商户订单号*/
    private String merchantOutOrderNo;
    /*商户号*/
    private String merid;
    /*随机字符串*/
    private String noncestr;
    /*平台订单*/
    private String orderNo;
    /*支付结果*/
    private String payResult;
    /*签名*/
    private String sign;
    /*支付渠道*/
    String channel;
    /*支付类型*/
    String key;
    /*通知链接*/
    String link;
    /*消息*/
    Msg msg;

    public NoticeRequest() {
    }

    public NoticeRequest(String merchantOutOrderNo, String merid, String noncestr, String orderNo, String payResult, String sign, String channel, String key, String link,Msg msg) {
        this.merchantOutOrderNo = merchantOutOrderNo;
        this.merid = merid;
        this.noncestr = noncestr;
        this.orderNo = orderNo;
        this.payResult = payResult;
        this.sign = sign;
        this.channel = channel;
        this.key = key;
        this.link = link;
        this.msg = msg;
    }

    public NoticeRequest(NoticeRequest noticeRequest) {
        this.merchantOutOrderNo = noticeRequest.getMerchantOutOrderNo();
        this.merid = noticeRequest.getMerid();
        this.noncestr = noticeRequest.getNoncestr();
        this.orderNo = noticeRequest.getOrderNo();
        this.payResult = noticeRequest.getPayResult();
        this.sign = noticeRequest.getSign();
        this.channel = noticeRequest.getChannel();
        this.key = noticeRequest.getKey();
        this.link = noticeRequest.getLink();
        this.msg = noticeRequest.getMsg();
    }

    /**
     * 构建对象
     */
    public static class Builder {

        private NoticeRequest target;

        public Builder() {
            target = new NoticeRequest();
        }

        public NoticeRequest.Builder merchantOutOrderNo(String merchantOutOrderNo) {
            target.merchantOutOrderNo = merchantOutOrderNo;
            return this;
        }

        public NoticeRequest.Builder merid(String merid) {
            target.merid = merid;
            return this;
        }

        public NoticeRequest.Builder noncestr(String noncestr) {
            target.noncestr = noncestr;
            return this;
        }

        public NoticeRequest.Builder orderNo(String orderNo) {
            target.orderNo = orderNo;
            return this;
        }

        public NoticeRequest.Builder payResult(String payResult) {
            target.payResult = payResult;
            return this;
        }

        public NoticeRequest.Builder sign(String sign) {
            target.sign = sign;
            return this;
        }

        public NoticeRequest.Builder channel(String channel) {
            target.channel = channel;
            return this;
        }

        public NoticeRequest.Builder key(String key) {
            target.key = key;
            return this;
        }

        public NoticeRequest.Builder link(String link) {
            target.link = link;
            return this;
        }

        public NoticeRequest.Builder msg(Msg msg) {
            target.msg = msg;
            return this;
        }

        public NoticeRequest build() {
            return new NoticeRequest(target);
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("merchantOutOrderNo=").append(merchantOutOrderNo);
        sb.append("&merid=").append(merid);
        sb.append("&noncestr=").append(noncestr);
        sb.append("&orderNo=").append(orderNo);
        sb.append("&payResult=").append(payResult);
        String sign = MD5.sign(sb.toString(), "&key=" + this.key, "utf-8");
        sb.append("&sign=" + sign);
        return sb.toString();
    }
}
