package com.ebeijia.zl.payment.request;

/**
 * 基础参数
 *
 * @author xiong.wang
 * @date 2017/6/19
 */
public class BaseRequest {
    /*商户订单号*/
    private String merchantOutOrderNo;
    /*商户号*/
    private String merid;
    /*随机字符串*/
    private String noncestr;
    /*签名*/
    private String sign;

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
}
