package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Api(value = "/pay", description = "用于定义支付、信用卡相关接口")
@RequestMapping(value = "/pay")
@RestController
public class PayController {
    @Autowired
    IPayService payService;

    @TokenCheck
    @ApiOperation("绑定银行卡")
    @RequestMapping(value = "/card/bind",method = RequestMethod.POST)
    public void bindBankCard() {

    }

    @TokenCheck
    @ApiOperation("列出银行卡")
    @RequestMapping(value = "/card/list",method = RequestMethod.GET)
    public void listAccountCard(@RequestParam("token") String token, @RequestParam("session") String session) {

    }

    //支付接口
    @TokenCheck
    @ApiOperation("支付订单")
    @RequestMapping(value = "/deal/order/{orderid}",method = RequestMethod.POST)
    public void payOrder(@PathVariable("orderid") String orderId, PayInfo payInfo, @RequestParam("session") String session) {
            payService.payOrder(payInfo,session);
    }

    @TokenCheck
    @ApiOperation("列出可用专项账户类型与余额")
    @RequestMapping(value = "/balance/list/{type}",method = RequestMethod.GET)
    public void listAccountDetail(@PathVariable(value = "type",required = false) String type,@RequestParam("session") String session) {

    }

    //交易流水
    @TokenCheck
    @ApiOperation("列出交易流水记录")
    @RequestMapping(value = "/deal/list/{type}",method = RequestMethod.GET)
    public void listAccountDeals(@PathVariable("type") String type, @RequestParam(value = "session", required = false) String session) {

    }

    //交易流水
    @TokenCheck
    @ApiOperation("时间戳筛选，列出交易流水记录")
    @RequestMapping(value = "/deal/list/",method = RequestMethod.GET)
    public void listAccountDealsWithTimestamp(String type, @RequestParam(value = "session", required = false) String session, Long begin,Long end) {
        payService.listDeals(type,begin,end);
    }

    /**
     * 托管账户转账
     * @return
     */
    @TokenCheck
    @ApiOperation("托管账户转出到银行卡")
    @ApiResponses({ @ApiResponse(code = ResultState.OK, message = "操作成功"),
            @ApiResponse(code = ResultState.ERROR, message = "服务器内部异常"),
            @ApiResponse(code = ResultState.FORBIDDEN, message = "权限不足"),
            @ApiResponse(code = ResultState.UNAUTHORIZED, message = "请重新登录")})
    @RequestMapping(value = "/deal/transfer",method = RequestMethod.POST)
    public JsonResult transferToCard(@Param("deal") DealInfo dealInfo, @Param(value = "session") Double session){
        if (session == null){
            session = 0D;
        }
        int state = payService.transferToCard(dealInfo,session);
        return new JsonResult(state);
    }

}
