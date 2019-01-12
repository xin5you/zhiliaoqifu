package com.ebeijia.zl.shop.service.supply.impl;

import com.ebeijia.zl.shop.service.supply.ISupplyService;
import org.springframework.web.client.RestTemplate;

public class SupplyService implements ISupplyService {



    @Override
    public Integer phoneCharge(String phone, Integer amount, String session) {
        RestTemplate rest = new RestTemplate();
        return 0;
    }
}
