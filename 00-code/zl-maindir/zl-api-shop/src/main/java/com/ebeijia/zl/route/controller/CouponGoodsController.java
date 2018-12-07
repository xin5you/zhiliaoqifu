package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api(value = "/coupon", description = "用于定义卡券类商品接口")
@RestController
@RequestMapping("/coupon")
public class CouponGoodsController {

    @ApiOperation("按类型的商品列表")
    @RequestMapping(value = "/list/cat{catid}/{order}",method = {RequestMethod.POST,RequestMethod.GET})
    public void listGoods(@PathVariable int catid, @PathVariable String order, int start, int limit){}

    @ApiOperation("商品详情")
    @RequestMapping(value = "/get/detail/{goods}",method = {RequestMethod.POST,RequestMethod.GET})
    public void goodsDetail(@PathVariable("goods") String goodsId){}

    @ApiOperation("获取图片")
    @RequestMapping(value = "/get/image/{id}",method = {RequestMethod.POST,RequestMethod.GET})
    public void goodsImage(@PathVariable("id") String imageId){}

}
