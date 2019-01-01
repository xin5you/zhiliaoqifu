package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.constants.PhoneValidMethod;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.service.valid.impl.ValidCodeService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.*;
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
    private IOrderService orderService;
    //订单状态机：    下单-支付中-A类支付完成-B类支付完成-发货-评价
    //                     取消  -A类退款       -B类退款 -退货-退货申请

    @Autowired
    ValidCodeService validCodeService;

    @Autowired
    ShopUtils shopUtils;


    //不经过购物车直接下单
    @TokenCheck(force = true)
    @ShopTransactional
    @ApiOperation("商品直接下单")
    @RequestMapping(value = "/goods/create/",method = RequestMethod.POST)
    public JsonResult<TbEcomPlatfOrder> createOrder(@ApiParam(name = "productId",value = "货品ID") String productId,@ApiParam(name = "amounts",value = "数量") Integer amounts,AddressInfo address, @RequestParam("session") String session){
        OrderItemInfo orderItemInfo = new OrderItemInfo();
        orderItemInfo.setProductId(productId);
        orderItemInfo.setAmount(amounts);
        TbEcomPlatfOrder simpleOrder = orderService.createSimpleOrder(orderItemInfo, address);
        return new JsonResult<>(simpleOrder);
    }

    @TokenCheck(force = true)
    @ApiOperation("订单支付确认")
    @RequestMapping(value = "/goods/apply/",method = RequestMethod.POST)
    public JsonResult<TbEcomPlatfOrder> orderNextState(PayInfo payInfo, String validCode){
        MemberInfo memberInfo = shopUtils.getSession();
        if (memberInfo == null) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "参数异常");
        }
        boolean valid = validCodeService.checkValidCode(PhoneValidMethod.PAY, memberInfo.getMobilePhoneNo(), validCode);
        if (!valid) {
            throw new AdviceMessenger(ResultState.NOT_ACCEPTABLE, "验证码有误");
        }
        TbEcomPlatfOrder platfOrder = orderService.applyOrder(payInfo);
        return new JsonResult<>(platfOrder);
    }

    @TokenCheck(force = true)
    @ApiOperation("订单取消")
    @RequestMapping(value = "/goods/cancel",method = RequestMethod.POST)
    public JsonResult<TbEcomPlatfOrder> orderCancel(String orderId){
        TbEcomPlatfOrder platfOrder = orderService.cancelOrder(orderId);
        return new JsonResult<>(platfOrder);
    }


    @TokenCheck(force = true)
    @ApiOperation("订单详情")
    @RequestMapping(value = "/goods/detail/{orderid}",method = RequestMethod.POST)
    public JsonResult<OrderDetailInfo> goodsOrderDetail(@PathVariable("orderid") String orderId){

        return new JsonResult<>();
    }

    @TokenCheck(force = true)
    @ApiOperation("订单列表")
    @RequestMapping(value = "/goods/list/{stat}",method = RequestMethod.GET)
    public JsonResult<PageInfo<OrderDetailInfo>> goodsOrderList(@PathVariable("stat") String orderStat, String orderby, Integer start, Integer limit){
        return new JsonResult<>();
    }


}
