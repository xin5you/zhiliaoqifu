package com.cn.thinkx.ecom.front.api.category.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.basics.goods.service.GoodsCategoryService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.front.api.category.service.CategoryService;

@RestController
@RequestMapping(value = "goods/category")
public class CategoryController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 渠道商城分类
	 * @param req
	 * @return
	 */
	@GetMapping("/listCategory")
	public ModelAndView listCategory(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("ecom/category/category");
		mv.addObject("footType", Constants.FootCodeType.CATE.getId());
		String ecomCode = req.getParameter("ecomCode");
		String defaultFirstCateGory = null;
		try {
//			//一级分类
			GoodsCategory firstCategory = new GoodsCategory();
			firstCategory.setCatPath("1");
			firstCategory.setParentId("0");
			firstCategory.setEcomCode(ecomCode);
			firstCategory.setListShow("1");
			List<GoodsCategory> firstList = goodsCategoryService.getGoodsCategory(firstCategory);
			defaultFirstCateGory = firstList.get(0).getCatId();
			mv.addObject("firstList", firstList);
			mv.addObject("defaultFirstCateGory", defaultFirstCateGory);
			mv.addObject("ecomCode",ecomCode);
		} catch (Exception e) {
			logger.error("###跳转渠道商城分类出错",e);
		}
		return mv;
	}
	
	/**
	 * 渠道商城商品信息
	 * @param req
	 * @return
	 */
	@PostMapping("/listGoods")
	public ModelMap listGoods(HttpServletRequest req,HttpServletResponse resp){
		ModelMap map = new ModelMap();
		String ecomCode = req.getParameter("ecomCode");
		String firstCateGory = req.getParameter("firstCateGory");
		String secondCategory = req.getParameter("secondCategory");
		try {
			Goods goods = new Goods();
			goods.setParentCatId(firstCateGory);
			goods.setEcomCode(ecomCode);
			goods.setCatId(secondCategory);
			List<Goods> goodsList = categoryService.getGoodsByGoods(goods);
			map.addAttribute(firstCateGory, goodsList);
		} catch (Exception e) {
			logger.error("###跳转渠道商城分类出错",e);
		}
		return map;
	}
	
	
	@PostMapping("/getSecondCategory")
	public ModelMap getSecondCategory(HttpServletRequest req){
		ModelMap map = new ModelMap();
		try {
			String catId = req.getParameter("catId");
			String ecomCode = req.getParameter("ecomCode");
			GoodsCategory goodsCategory = new GoodsCategory();
			goodsCategory.setCatPath("2");
			goodsCategory.setParentId(catId);
			goodsCategory.setEcomCode(ecomCode);
			goodsCategory.setListShow("1");
			List<GoodsCategory> secondList = goodsCategoryService.getGoodsCategory(goodsCategory);
			map.addAttribute("secondList", secondList);
		} catch (Exception e) {
			logger.error("###查询二级分类出错",e);
		}
		return map;
	}
	
}
