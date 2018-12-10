package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 */
@Api(value = "/order",description = "用户定义订单相关接口")
@RequestMapping(value = "/order",method = RequestMethod.POST)
@RestController
public class OrderController {
    @Autowired
    IOrderService iOrderService;
    //订单状态机：    下单-支付中-A类支付完成-B类支付完成-发货-评价
    //                     取消  -A类退款       -B类退款 -退货-退货申请


    //不经过购物车直接下单
    @TokenCheck
    @ApiOperation("商品直接下单")
    @RequestMapping("/goods/create/{sku}")
    public void createOrder(@PathVariable("sku") String sku,@RequestParam("token") String token,@RequestParam("session") String session){

    }

    @TokenCheck
    @ApiOperation("订单支付确认")
    @RequestMapping("/goods/apply/{orderid}")
    public void orderNextState(@PathVariable("orderid") String orderId, @RequestParam("token") String token, PayInfo payInfo, @RequestParam("session") String session){

    }

    @TokenCheck
    @ApiOperation("订单状态修改")
    @RequestMapping("/goods/update/{orderid}")
    public void orderReplaceState(@PathVariable("orderid") String orderId,@RequestParam String token){

    }

    @TokenCheck
    @ApiOperation("订单详情")
    @RequestMapping("/goods/detail/{orderid}")
    public void goodsDetail(@PathVariable("orderid") String orderId,@RequestParam String token){}


}
