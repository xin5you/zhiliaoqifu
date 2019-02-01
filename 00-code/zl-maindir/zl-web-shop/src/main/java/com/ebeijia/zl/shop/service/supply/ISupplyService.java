package com.ebeijia.zl.shop.service.supply;

import com.ebeijia.zl.shop.vo.PayInfo;
import com.fasterxml.jackson.databind.JsonNode;

public interface ISupplyService {

    String phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);

    String phoneCharge(String memberId, String phone, Long amount, PayInfo payInfo);

    String getPhoneChargeProvider();

    Integer phoneChargeCallback(JsonNode respVO);

    String outerPayPhoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);
}
