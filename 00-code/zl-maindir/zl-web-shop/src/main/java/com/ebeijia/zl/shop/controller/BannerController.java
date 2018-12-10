package com.ebeijia.zl.shop.controller;

import com.ebeijia.zl.shop.service.banner.IBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @User J
 * @Date 2018/12/05
 */
@Api(value = "/banner",description = "定义Banner输出的相关接口")
@RequestMapping("/banner")
@RestController
public class BannerController {

    @Autowired
    IBannerService bannerService;

    @ApiOperation("获取图片")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public void goodsImage(String pos,String spec,Integer index){

    }



}
