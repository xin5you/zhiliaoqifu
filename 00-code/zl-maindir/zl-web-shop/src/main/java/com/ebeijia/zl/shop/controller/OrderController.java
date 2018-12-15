package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderInf;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.JsonResult;
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
    public JsonResult<TbEcomOrderInf> createOrder(@PathVariable("sku") String sku, @RequestParam("session") String session){
        return new JsonResult<>(new TbEcomOrderInf());
    }

    @TokenCheck
    @ApiOperation("订单支付确认")
    @RequestMapping("/goods/apply/{orderid}")
    public JsonResult<TbEcomOrderInf> orderNextState(@PathVariable("orderid") String orderId, PayInfo payInfo, @RequestParam("session") String session){
        return new JsonResult<>(new TbEcomOrderInf());
    }

    @TokenCheck
    @ApiOperation("订单状态修改")
    @RequestMapping("/goods/update/{orderid}")
    public JsonResult<Object> orderReplaceState(@PathVariable("orderid") String orderId, @RequestParam String token){
        return new JsonResult<>().setCode(200);
    }

    @TokenCheck
    @ApiOperation("订单详情")
    @RequestMapping("/goods/detail/{orderid}")
    public JsonResult<TbEcomOrderInf> goodsDetail(@PathVariable("orderid") String orderId, @RequestParam String token){
        return new JsonResult<>(new TbEcomOrderInf());
    }


}
