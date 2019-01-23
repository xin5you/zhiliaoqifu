package com.ebeijia.zl.payment.core.entity;

import com.ebeijia.zl.payment.core.constant.PayConstant;

/**
 * 易生支付
 *
 * @author xiong.wang
 * @date 2017/7/14
 */
public class EasyPayPushOrder {
    private String partnerId = "900029000000298";
    private String merchantId = "900029000000298";
    private String signKey = "J1FPZ23E7N5yuk2IGtkk";
    private String encKey = "vIlOpGnCmCkimMqxQW0zYIoO";
    private long amount = 1;
    private String payType = "jsPay";
    private String orderId;
    private long businessTime = System.currentTimeMillis();
    private String notifyUrl;
    private String orderDesc = "芸券平台商户订单";
    private String merchantName;
    private String subMchId;
    private String frontUrl;
    private String orderNo;
    private String version = "2.1";
    private String serviceType = "pushOrder";
    private String url = PayConstant.PX_ONLINE_URL;
    private String openId;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(long businessTime) {
        this.businessTime = businessTime;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
