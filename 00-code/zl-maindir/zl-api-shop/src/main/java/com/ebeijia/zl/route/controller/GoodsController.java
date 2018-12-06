package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api("用于定义商品相关接口")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @ApiOperation("商品列表，分页")
    @RequestMapping("/list/{orderby}")
    public void listGoods(@PathVariable String orderby,int start,int limit){

    }


    @ApiOperation("按类型的商品列表")
    @RequestMapping("/list/cat{catid}/{orderby}")
    public void listGoods(@PathVariable int catid, @PathVariable String orderby,int start,int limit){}

    @ApiOperation("商品详情")
    @RequestMapping("/get/detail/{goods}")
    public void goodsDetail(@PathVariable("goods") String goodsId){}

    @ApiOperation("商品相册")
    @RequestMapping("/get/gallery/{goods}")
    public void goodsGallery(@PathVariable("goods") String goodsId){}

    @ApiOperation("获取图片")
    @RequestMapping("/get/image/{id}")
    public void goodsImage(@PathVariable("id") String imageId){}

    //
    //
    //
}
