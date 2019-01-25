package com.ebeijia.zl.shop.service.pay;

import com.ebeijia.zl.shop.vo.*;

import javax.validation.Valid;

public interface IWxPayService {

    String wxPaySuccess(@Valid WxPaySuccessDTO success);

    String wxPayBack(@Valid WxPayBackDTO back);

    String applyPay(WxPayReqDTO payInfo);

    boolean queryYiMaTongFuOrderMsg(String orderID, int mchtID, String merKey);

    String callback(String payResult, String dmsKey, String orderNo);

    WxPayBackMsgDTO checkData(int mchtId, WxPayBackDTO back);

    RequestWxQueryDTO weChatPayQuery(String merchantOutOrderNo);
}
