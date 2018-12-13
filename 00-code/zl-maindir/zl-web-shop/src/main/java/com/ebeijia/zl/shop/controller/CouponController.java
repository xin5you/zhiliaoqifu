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
    @RequestMapping(value = "/list/cat{catid}",method = RequestMethod.GET)
    public JsonResult<List<TbEcomGoods>> listGoods(@PathVariable Integer catid, String orderby, Integer start, Integer limit){
        List<TbEcomGoods> list = couponService.listGoods(catid,orderby,start,limit);
        return new JsonResult<>(list);

    }

    @ApiOperation("卡券详情")
    @RequestMapping(value = "/get/detail/{goods}",method = RequestMethod.GET)
    public JsonResult<TbEcomGoodsDetail> goodsDetail(@PathVariable("goods") String goodsId){
        TbEcomGoodsDetail detail = couponService.goodsDetail(goodsId);
        return new JsonResult<>(detail);
    }


    @ApiOperation("卡券转让")
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public JsonResult checkVaild(@ApiParam("验证码") @RequestParam("vaildcode") String vaildCode) {
        int code = couponService.checkVaild(vaildCode);
        return new JsonResult<>(200);
    }

}
