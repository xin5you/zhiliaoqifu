package com.ebeijia.zl.shop.service.supply;

import com.ebeijia.zl.shop.vo.PayInfo;

import java.util.LinkedHashMap;

public interface ISupplyService {

    String phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);

    String phoneCharge(String memberId, String phone, Integer amount, PayInfo payInfo);

    String getPhoneChargeProvider();

    Integer phoneChargeCallback(LinkedHashMap<String, String> respVO);

    String outerPayPhoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);
}
