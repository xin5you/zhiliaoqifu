package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.ebeijia.zl.shop.vo.GoodsDetailInfo;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api(value = "/goods",description = "用于定义商品相关接口")
@RequestMapping("/goods")
@RestController
public class GoodsController {
    @Autowired
    IProductService goodsService;

    @ApiOperation("所有商品列表，分页")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult<PageInfo<Goods>> listGoods(String orderby, Integer start, Integer limit){
        //TODO change VO design, vaild variable;

        PageInfo<Goods> listGoods = goodsService.listGoods(null, null, orderby, start, limit);
        return new JsonResult<>(listGoods);
    }

    @ApiOperation("按专项类型的商品列表")
    @RequestMapping(value = "/list/{billingtype}",method = RequestMethod.GET)
    public JsonResult<PageInfo<Goods>> listGoods(@PathVariable("billingtype") String billingType, String orderby, Integer start, Integer limit){
        PageInfo<Goods> list = goodsService.listGoods(billingType,orderby,start,limit);

        return new JsonResult<>(list);
    }

    @ApiOperation("查找特定的商品列表（一期不用）")
    @RequestMapping(value = "/list/cat{catid}",method = RequestMethod.GET)
    public JsonResult<PageInfo<Goods>> listGoods(@PathVariable String catid,@RequestParam("billingtype") String billingType,  String orderby, Integer start, Integer limit){
        PageInfo<Goods> list = goodsService.listGoods(billingType,catid,orderby,start,limit);
        return new JsonResult<PageInfo<Goods>>(list);
    }

    @ApiOperation("商品详情")
    @RequestMapping(value = "/detail/get/{goods}",method = RequestMethod.GET)
    public JsonResult<GoodsDetailInfo> goodsDetail(@PathVariable("goods") String goodsId){
        GoodsDetailInfo detail = goodsService.getDetail(goodsId);
        return new JsonResult<>(detail);
    }

    @ApiOperation("商品相册")
    @RequestMapping(value = "/gallery/get/{goods}",method = RequestMethod.GET)
    public JsonResult<List<TbEcomGoodsGallery>> goodsGallery(@PathVariable("goods") String goodsId){
        List<TbEcomGoodsGallery> goodsGallery = goodsService.getGallery(goodsId);
        return new JsonResult<>(goodsGallery);
    }

//
//    @ApiOperation("获取图片")
//    @RequestMapping(value = "/image/get/{id}",method = RequestMethod.GET)
//    public void getImage(@PathVariable("id") String imageId){}

    @ApiOperation("商品对应货品查询、库存查询")
    @RequestMapping(value = "/amount/get/{goods}",method = RequestMethod.GET)
    public JsonResult<List<TbEcomGoodsProduct>> goodsAvailableAmount(@PathVariable("goods") String goodsId){
        List<TbEcomGoodsProduct> detail = goodsService.listSkuByGoodsId(goodsId);
        return new JsonResult<>(detail);
    }


    /**
     *
     SELECT
     GOODSID
     FROM
     EMALL_GOODS_SPEC
     WHERE
     CLASSID=669 AND
     ((SPECID = 5 AND SPEC_VALUEID = 9)
     OR (SPECID = 6 AND SPEC_VALUEID = 51)
     OR (SPECID = 3 AND SPEC_VALUEID = 5))
     GROUP BY
     GOODSID
     HAVING
     COUNT (*) = 3
     */
    //
    //
}
