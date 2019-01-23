package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class WxPayBackDTO {
    @NotBlank
    @ApiModelProperty("商户订单号")
    private String merchantOutOrderNo;

    @ApiModelProperty("商户号")
    private String merid;

    @NotBlank
    @ApiModelProperty("订单的详细信")
    private String msg;

    @ApiModelProperty("随机参数")
    private String noncestr;

    @ApiModelProperty("平台订单号")
    private String orderNo;

    @NotBlank
    @ApiModelProperty("支付结果")
    private String payResult;

    @NotBlank
    @ApiModelProperty("签名结果")
    private String sign;

    /**
     * 获取拼接后的签名MD5
     *
     * @return
     */
    public String getSignMD5(String key) {
        StringBuilder param = new StringBuilder();
        param.append("merchantOutOrderNo" + "=" + merchantOutOrderNo);
        param.append("&merid" + "=" + merid);
        param.append("&msg" + "=" + msg);
        param.append("&noncestr" + "=" + noncestr);
        param.append("&orderNo" + "=" + orderNo);
        param.append("&payResult" + "=" + payResult);
        param.append("&key" + "=" + key);
        return DigestUtils.md5Hex(param.toString());
    }

    public String signUrlParam(String key) {
        StringBuilder param = new StringBuilder();
        param.append("merchantOutOrderNo" + "=" + merchantOutOrderNo);
        param.append("&merid" + "=" + merid);
        param.append("&msg" + "=" + msg);
        param.append("&noncestr" + "=" + noncestr);
        param.append("&orderNo" + "=" + orderNo);
        param.append("&payResult" + "=" + payResult);
        param.append("&sign" + "=" + getSignMD5(key));
        return param.toString();
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    @Override
    public String toString() {
        return "WxPayBackDTO{" +
                "merchantOutOrderNo='" + merchantOutOrderNo + '\'' +
                ", merid='" + merid + '\'' +
                ", msg='" + msg + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", payResult='" + payResult + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
