package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.service.coupon.ICouponService;
import com.ebeijia.zl.shop.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api(value = "/coupon", description = "用于定义卡券类商品接口")
@RequestMapping("/coupon")
@RestController
public class CouponController {
    ICouponService couponService;

    @ApiOperation("按类型的卡券列表")
    @RequestMapping(value = "/list/cat{catid}/{order}",method = {RequestMethod.POST,RequestMethod.GET})
    public void listGoods(@PathVariable int catid, @PathVariable String order, int start, int limit){
        List<TbEcomGoods> list = couponService.listGoods(catid,order,start,limit);
    }

    @ApiOperation("卡券详情")
    @RequestMapping(value = "/get/detail/{goods}",method = {RequestMethod.POST,RequestMethod.GET})
    public void goodsDetail(@PathVariable("goods") String goodsId){
        TbEcomGoodsDetail detail = couponService.goodsDetail(goodsId);
    }

    @ApiOperation("获取图片")
    @RequestMapping(value = "/get/image/{id}",method = {RequestMethod.POST,RequestMethod.GET})
    public void goodsImage(@PathVariable("id") String imageId){

    }

    @ApiOperation("卡券转让")
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public JsonResult checkVaild(@ApiParam("验证码") @RequestParam("vaildcode") String vaildCode) {
        int code = couponService.checkVaild(vaildCode);
        return new JsonResult<>(200);
    }

}
