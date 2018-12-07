package com.ebeijia.zl.route.controller;

import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "/pay",description = "用于定义支付、信用卡相关接口")
@RestController
@RequestMapping(value = "/pay",method = RequestMethod.POST)
public class PayController {

    @ApiOperation("绑定银行卡")
    @RequestMapping(value = "/card/bind")
    public void bindBankCard(){}

    @ApiOperation("列出银行卡")
    @RequestMapping(value = "/card/list")
    public void listAccountCard(@RequestParam("token") String token, @RequestParam("session") String session){

    }

    //支付接口
    @ApiOperation("支付订单")
    @RequestMapping(value = "/order/{orderid}")
    public void payOrder(@PathVariable("orderid") String orderId, @RequestParam("token")
            String token, PayInfo payInfo, @RequestParam("session") String session){

    }

    @ApiOperation("余额检查")
    @RequestMapping(value = "/list")
    public void listAccountDetail(@RequestParam("token") String token, @RequestParam("session") String session){

    }

    //检查订单余额

}
