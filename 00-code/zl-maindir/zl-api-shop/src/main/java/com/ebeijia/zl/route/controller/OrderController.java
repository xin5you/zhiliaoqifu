package com.ebeijia.zl.route.controller;

import com.ebeijia.zl.shop.vo.PayInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 */
@Api("用户定义订单相关接口")
@RestController
@RequestMapping(value = "/order",method = RequestMethod.POST)
public class OrderController {
    //订单状态机：    下单-支付中-A类支付完成-B类支付完成-发货-评价
    //                     取消  -A类退款       -B类退款 -退货-退货申请


    //
    @ApiOperation("商品订单创建")
    @RequestMapping("/goods/create/{goodsid}")
    public void createOrder(@PathVariable("goodsid") String goodsId,String token){

    }

    @ApiOperation("订单支付")
    @RequestMapping("/goods/apply/{orderid}/")
    public void orderNextState(@PathVariable("orderid") String orderId,String token,PayInfo payInfo){

    }

    @ApiOperation("订单状态修改")
    @RequestMapping("/goods/update/{orderid}")
    public void orderReplaceState(@PathVariable("orderid") String orderId,String token){

    }


}
