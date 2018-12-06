package com.ebeijia.zl.route.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public void listGoods(){}

    @ApiOperation("商品详情")
    public void goodsDetail(){}

    @ApiOperation("商品相册")
    public void goodsGallery(){}

    //
    //
    //
}
