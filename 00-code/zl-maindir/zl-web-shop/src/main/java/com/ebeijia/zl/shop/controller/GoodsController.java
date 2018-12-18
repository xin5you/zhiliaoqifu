package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsGallery;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.ebeijia.zl.shop.vo.JsonResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("商品列表，分页")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult<PageInfo<TbEcomGoods>> listGoods(String orderby, Integer start, Integer limit){
        //TODO change VO design, vaild variable;
        PageInfo<TbEcomGoods> list = goodsService.listGoods(null,orderby,start,limit);
        return new JsonResult<PageInfo<TbEcomGoods>>(list);
    }

    @ApiOperation("按类型的商品列表")
    @RequestMapping(value = "/list/cat{catid}",method = RequestMethod.GET)
    public JsonResult<PageInfo<TbEcomGoods>> listGoods(@PathVariable Integer catid, String orderby, Integer start, Integer limit){
        PageInfo<TbEcomGoods> list = goodsService.listGoods(catid,orderby,start,limit);
        return new JsonResult<PageInfo<TbEcomGoods>>(list);
    }

    @ApiOperation("商品详情")
    @RequestMapping(value = "/detail/get/{goods}",method = RequestMethod.GET)
    public JsonResult<TbEcomGoodsDetail> goodsDetail(@PathVariable("goods") String goodsId){
        TbEcomGoodsDetail detail = goodsService.getDetail(goodsId);
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
    //
    //
}
