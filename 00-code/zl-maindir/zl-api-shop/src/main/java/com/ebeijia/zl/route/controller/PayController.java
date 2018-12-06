package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("用于定义支付、信用卡相关接口")
@RestController
@RequestMapping("/pay")
public class PayController {

    @ApiOperation("绑定银行卡")
    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    public void bindBankCard(){}

    //支付接口
    @ApiOperation("支付订单")
    @RequestMapping(value = "/order",method = RequestMethod.POST)
    public void payOrder(){}



    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public void listAccountDetail(){}

    //检查订单余额

}
