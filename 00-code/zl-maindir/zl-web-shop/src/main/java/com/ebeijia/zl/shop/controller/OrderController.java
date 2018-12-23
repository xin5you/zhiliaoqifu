package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderInf;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.EcomOrderDetailInfo;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 */
@Api(value = "/order",description = "用户定义订单相关接口")
@RequestMapping(value = "/order")
@RestController
public class OrderController {
    @Autowired
    IOrderService orderService;
    //订单状态机：    下单-支付中-A类支付完成-B类支付完成-发货-评价
    //                     取消  -A类退款       -B类退款 -退货-退货申请


    //不经过购物车直接下单
    @TokenCheck(force = true)
    @ShopTransactional
    @ApiOperation("商品直接下单")
    @RequestMapping(value = "/goods/create/",method = RequestMethod.POST)
    public JsonResult<TbEcomOrderInf> createOrder(@ApiParam(name = "productId",value = "货品ID") String productId,@ApiParam(name = "amounts",value = "数量") Integer amounts,AddressInfo address, @RequestParam("session") String session){
        TbEcomPlatfShopOrder order = orderService.createSimpleOrder(productId,amounts,address);
        return new JsonResult<>(new TbEcomOrderInf());
    }

    @TokenCheck(force = true)
    @ApiOperation("订单支付确认")
    @RequestMapping(value = "/goods/apply/{orderid}",method = RequestMethod.POST)
    public JsonResult<TbEcomOrderInf> orderNextState(@PathVariable("orderid") String orderId, AddressInfo address, PayInfo payInfo, @RequestParam("session") String session){
        return new JsonResult<>(new TbEcomOrderInf());
    }

    @TokenCheck(force = true)
    @ApiOperation("订单状态修改")
    @RequestMapping(value = "/goods/update/{orderid}",method = RequestMethod.POST)
    public JsonResult<Object> orderReplaceState(@PathVariable("orderid") String orderId){
        return new JsonResult<>().setCode(200);
    }

    @TokenCheck(force = true)
    @ApiOperation("订单详情")
    @RequestMapping(value = "/goods/detail/{orderid}",method = RequestMethod.POST)
    public JsonResult<EcomOrderDetailInfo> goodsOrderDetail(@PathVariable("orderid") String orderId){
        return new JsonResult<>();
    }

    @TokenCheck(force = true)
    @ApiOperation("订单列表")
    @RequestMapping(value = "/goods/list/{stat}",method = RequestMethod.GET)
    public JsonResult<PageInfo<TbEcomPlatfOrder>> goodsOrderList(@PathVariable("stat") String orderStat, String orderby, Integer start, Integer limit){
        return new JsonResult<>();
    }


}
