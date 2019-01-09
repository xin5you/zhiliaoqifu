package com.ebeijia.zl.coupon.controller;

import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.service.ICouponService;
import com.ebeijia.zl.shop.utils.TokenCheck;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "/mapper/coupon", description = "用于定义卡券类商品接口")
@RequestMapping("/mapper/coupon")
@RestController
public class CouponController {

    @Autowired
    ICouponService couponService;

    //列出某类型的卡商品
    @ApiOperation("按类型的卡券列表")
    @RequestMapping(value = "/list/{bId}",method = RequestMethod.GET)
    public JsonResult<PageInfo<TbCouponProduct>> listProduct(@PathVariable String bId, String orderby, Integer start, Integer limit){
        PageInfo<TbCouponProduct> list = couponService.listProduct(bId,orderby,start,limit);
        return new JsonResult<>(list);
    }

    //查找指定卡的信息、折损率
    @ApiOperation("查找指定卡商品信息、折损率")
    @RequestMapping(value = "/get/detail/{couponCode}",method = RequestMethod.GET)
    public JsonResult<TbCouponProduct> goodsDetail(@PathVariable("couponCode") String couponCode){
        TbCouponProduct detail = couponService.couponDetail(couponCode);
        return new JsonResult<>(detail);
    }


    //转让指定数量的卡
    @TokenCheck(force = true)
    @ApiOperation("卡券转让")
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public JsonResult<String> couponShare(@RequestParam String couponCode,@RequestParam Long price,@RequestParam Integer amount,@ApiParam("验证码") @RequestParam("vaildcode") String vaildCode) {
        int code = couponService.couponShare(vaildCode,couponCode,price,amount);
        return new JsonResult<>(code,"");
    }

    //转让指定数量的卡
    @TokenCheck(force = true)
    @ApiOperation("卡券购买")
    @RequestMapping(value = "/buy",method = RequestMethod.POST)
    public JsonResult<String> buyCoupon(@RequestParam String couponCode,@RequestParam Integer amount,@ApiParam("验证码") @RequestParam(value = "vaildcode",required = false) String vaildCode) {
        int code = couponService.buyCoupon(vaildCode,couponCode,amount);
        return new JsonResult<>(code,"");
    }


    @TokenCheck(force = true)
    @ApiOperation("列出某类型的卡券持有情况")
    @RequestMapping(value = "/list/holder/{bId}",method = RequestMethod.GET)
    public JsonResult<PageInfo<TbCouponHolder>> getHolder(@PathVariable String bId, Integer start, Integer limit) {
        PageInfo<TbCouponHolder> holder = couponService.getHolder(bId,start,limit);
        return new JsonResult<>(holder);
    }

    //查找指定卡的持有情况
    @TokenCheck(force = true)
    @ApiOperation("列出指定卡券持有情况")
    @RequestMapping(value = "/get/holder/{couponCode}",method = RequestMethod.GET)
    public JsonResult<TbCouponHolder> getHolderByCouponCode(@PathVariable String couponCode,@RequestParam Long price) {
        TbCouponHolder holder = couponService.getHolderByCouponCode(couponCode,price);
        return new JsonResult<>(holder);
    }


}


