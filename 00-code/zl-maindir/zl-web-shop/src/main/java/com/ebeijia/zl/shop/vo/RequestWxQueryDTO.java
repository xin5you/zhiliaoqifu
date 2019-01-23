package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询---益码通付---返回结果")
public class RequestWxQueryDTO {
    @ApiModelProperty("请求支付商户ID")
    private String merid;

    @ApiModelProperty("请求支付的订单号")
    private String merchantOutOrderNo;

    @ApiModelProperty("请求支付的订单号")
    private String orderMoney;

    @ApiModelProperty("支付订单号")
    private String orderNo;

    @ApiModelProperty("微信交易号")
    private String tradeNo;

    @ApiModelProperty("银行交易号")
    private String thirdNo;

    @ApiModelProperty("支付结果")
    private String payResult;

    @ApiModelProperty("支付时间")
    private String payTime;

    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }

    public String getMerchantOutOrderNo() {
        return merchantOutOrderNo;
    }

    public void setMerchantOutOrderNo(String merchantOutOrderNo) {
        this.merchantOutOrderNo = merchantOutOrderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getThirdNo() {
        return thirdNo;
    }

    public void setThirdNo(String thirdNo) {
        this.thirdNo = thirdNo;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
