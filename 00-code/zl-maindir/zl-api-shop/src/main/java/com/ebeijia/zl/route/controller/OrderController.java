package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 */
@Api("用户定义订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    //订单状态机：    下单-支付中-A类支付完成-B类支付完成-发货-评价
    //                     取消  -A类退款       -B类退款 -退货-退货申请


    //
    @ApiOperation("商品订单创建")
    @RequestMapping("/goods/create")
    public void createOrder(){

    }

    @ApiOperation("订单支付")
    @RequestMapping("/goods/apply/{id}/")
    public void orderNextState(@PathVariable("id") String orderId){

    }

    @ApiOperation("订单状态修改")
    @RequestMapping("/goods/update/{id}")
    public void orderReplaceState(@PathVariable("id") String orderId){

    }


}
