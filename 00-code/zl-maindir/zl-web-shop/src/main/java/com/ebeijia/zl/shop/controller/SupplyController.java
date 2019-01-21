package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.supply.ISupplyService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.ebeijia.zl.shop.vo.TeleRespVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    private ISupplyService supplyService;

    //直充接口
    @TokenCheck(force = true)
    @ApiOperation("手机充值接口")
    @RequestMapping(value = "/phone/charge", method = RequestMethod.POST)
    public JsonResult<Integer> phoneCharge(String phone, Integer amount, String validCode, PayInfo payInfo,String session){
        Integer i = supplyService.phoneCharge(phone,amount,validCode,payInfo,session);
        return new JsonResult<>(i);
    }


    //直充接口
    @ApiOperation("直充回调接口")
    @RequestMapping(value = "/phone/charge/callback", method = RequestMethod.POST)
    public JsonResult<Integer> phoneChargeCallback(TeleRespVO respVO){
        Integer i = supplyService.phoneChargeCallback(respVO);
        return new JsonResult<>(i);
    }


}
