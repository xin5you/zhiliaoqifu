package com.ebeijia.zl.shop.service.supply;

import com.ebeijia.zl.shop.vo.PayInfo;
import com.ebeijia.zl.shop.vo.TeleRespVO;

public interface ISupplyService {

    Integer phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);

    String getPhoneChargeProvider();

    Integer phoneChargeCallback(TeleRespVO respVO);

}
