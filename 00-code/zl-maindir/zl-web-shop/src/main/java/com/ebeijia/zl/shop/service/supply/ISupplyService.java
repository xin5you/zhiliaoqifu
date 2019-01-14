package com.ebeijia.zl.shop.service.supply;

import com.ebeijia.zl.shop.vo.PayInfo;

public interface ISupplyService {

    Integer phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo, String session);
}
