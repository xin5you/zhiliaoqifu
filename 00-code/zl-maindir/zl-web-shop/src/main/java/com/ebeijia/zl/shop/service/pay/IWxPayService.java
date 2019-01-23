package com.ebeijia.zl.shop.service.pay;

public interface IWxPayService {
    String callback(String payResult, String dmsKey, String orderNo);
}
