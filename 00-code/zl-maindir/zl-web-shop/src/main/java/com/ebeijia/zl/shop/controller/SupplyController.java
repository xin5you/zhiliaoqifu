package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.supply.ISupplyService;
import com.ebeijia.zl.shop.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    private ISupplyService supplyService;

    //直充接口
    public JsonResult<Integer> phoneCharge(String phone,Integer amount,String session){
        Integer i = supplyService.phoneCharge(phone,amount,session);
        return new JsonResult<>(i);
    }


}
