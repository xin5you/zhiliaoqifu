package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
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


    //订单创建
    public void createOrder(){

    }

    //订单状态变更
    public void orderNextState(){

    }
    //订单状态
    public void orderReplaceState(){

    }
}
