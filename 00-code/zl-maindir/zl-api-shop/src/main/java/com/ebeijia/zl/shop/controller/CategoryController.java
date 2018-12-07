package com.ebeijia.zl.shop.controller;


import com.ebeijia.zl.shop.constants.GoodsType;
import com.ebeijia.zl.shop.dao.category.ICategoryService;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsCategory;
import com.ebeijia.zl.shop.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/category",description = "用于定义分类信息查询接口")
@RequestMapping(value = "/category")
@RestController
public class CategoryController {

    @Autowired
    ICategoryService categoryService;
    /**
     * 常规商品分类
     * @return
     */
    @ApiOperation("获取普通商品分类")
    @RequestMapping(value = "/goods/list",method = RequestMethod.GET)
    public JsonResult listGoodsCategory(){
        List<TbEcomGoodsCategory> list = categoryService.listCategory(GoodsType.NORMAL);
        return null;
    }

    /**
     * 卡券分类
     * @return
     */
    @ApiOperation("获取卡券商品分类")
    @RequestMapping(value = "/coupon/list",method = RequestMethod.GET)
    public JsonResult listCouponCategory(){
        List<TbEcomGoodsCategory> list = categoryService.listCategory(GoodsType.COUPON);
        return null;
    }


}
