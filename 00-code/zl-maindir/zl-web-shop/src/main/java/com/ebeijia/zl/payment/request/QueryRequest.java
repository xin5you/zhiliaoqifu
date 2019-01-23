package com.ebeijia.zl.payment.request;


import com.ebeijia.zl.payment.util.MD5;

import java.io.Serializable;

/**
 * 订单查询
 *
 * @author xiong.wang
 * @date 2017/6/19
 */
public class QueryRequest implements Serializable {
    /*商户订单号*/
    private String merchantOutOrderNo;
    /*商户号*/
    private String merid;
    /*随机字符串*/
    private String noncestr;
    /*签名*/
    private String sign;
    /*支付渠道*/
    String channel;
    /*支付类型*/
    String key;
    /*通知链接*/
    String link;

    public QueryRequest() {
    }

    public QueryRequest(String merchantOutOrderNo, String merid, String noncestr, String sign, String channel, String key, String link) {
        this.merchantOutOrderNo = merchantOutOrderNo;
        this.merid = merid;
        this.noncestr = noncestr;
        this.sign = sign;
        this.channel = channel;
        this.key = key;
        this.link = link;
    }

    public QueryRequest(QueryRequest noticeRequest) {
        this.merchantOutOrderNo = noticeRequest.getMerchantOutOrderNo();
        this.merid = noticeRequest.getMerid();
        this.noncestr = noticeRequest.getNoncestr();
        this.sign = noticeRequest.getSign();
        this.channel = noticeRequest.getChannel();
        this.key = noticeRequest.getKey();
        this.link = noticeRequest.getLink();
    }

    /**
     * 构建对象
     */
    public static class Builder {

        private QueryRequest target;

        public Builder() {
            target = new QueryRequest();
        }

        public QueryRequest.Builder merchantOutOrderNo(String merchantOutOrderNo) {
            target.merchantOutOrderNo = merchantOutOrderNo;
            return this;
        }

        public QueryRequest.Builder merid(String merid) {
            target.merid = merid;
            return this;
        }

        public QueryRequest.Builder noncestr(String noncestr) {
            target.noncestr = noncestr;
            return this;
        }

        public QueryRequest.Builder sign(String sign) {
            target.sign = sign;
            return this;
        }

        public QueryRequest.Builder channel(String channel) {
            target.channel = channel;
            return this;
        }

        public QueryRequest.Builder key(String key) {
            target.key = key;
            return this;
        }

        public QueryRequest.Builder link(String link) {
            target.link = link;
            return this;
        }

        public QueryRequest build() {
            return new QueryRequest(target);
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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("merchantOutOrderNo=").append(merchantOutOrderNo);
        sb.append("&merid=").append(merid);
        sb.append("&noncestr=").append(noncestr);
        String sign = MD5.sign(sb.toString(), "&key=" + this.key, "utf-8");
        sb.append("&sign=" + sign);
        return sb.toString();
    }
}
