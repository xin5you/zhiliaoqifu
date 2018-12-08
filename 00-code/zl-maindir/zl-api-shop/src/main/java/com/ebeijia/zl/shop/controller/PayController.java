package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "/pay", description = "用于定义支付、信用卡相关接口")
@RequestMapping(value = "/pay")
@RestController
public class PayController {
    @Autowired
    IPayService payService;

    @ApiOperation("绑定银行卡")
    @RequestMapping(value = "/card/bind",method = RequestMethod.POST)
    public void bindBankCard() {
    }

    @ApiOperation("列出银行卡")
    @RequestMapping(value = "/card/list",method = RequestMethod.GET)
    public void listAccountCard(@RequestParam("token") String token, @RequestParam("session") String session) {
    }

    //支付接口
    @ApiOperation("支付订单")
    @RequestMapping(value = "/deal/order/{orderid}",method = RequestMethod.POST)
    public void payOrder(@PathVariable("orderid") String orderId, @RequestParam("token")
            String token, PayInfo payInfo, @RequestParam("session") String session) {

    }

    @ApiOperation("列出可用专项账户类型与余额")
    @RequestMapping(value = "/balance/list/{type}",method = RequestMethod.GET)
    public void listAccountDetail(@RequestParam("token") String token, @PathVariable(value = "type",required = false) String type,@RequestParam("session") String session) {

    }

    //交易流水
    @ApiOperation("列出交易流水记录")
    @RequestMapping(value = "/deal/list/{type}",method = RequestMethod.GET)
    public void listAccountDeals(@PathVariable("type") String type, @RequestParam("token")
            String token, @RequestParam(value = "session", required = false) String session) {

    }

    /**
     * 托管账户转账
     * @return
     */
    @ApiOperation("托管账户转出到银行卡")
    @RequestMapping(value = "/deal/transfer",method = RequestMethod.POST)
    public JsonResult transferToCard(@RequestParam("deal") DealInfo dealInfo, @RequestParam("token")
                                                 String token, @RequestParam(value = "session", required = false) String session){
        int state = payService.transferToCard(token,dealInfo);
        return null;
    }

}
