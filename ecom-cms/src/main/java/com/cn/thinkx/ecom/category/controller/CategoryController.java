package com.cn.thinkx.ecom.category.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsCategory;
import com.cn.thinkx.ecom.category.service.CategoryService;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsCategoryListShowType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.vo.goods.APICategoryTO;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "ecom/category")
public class CategoryController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("yxOpenApiService")
	private YXOpenApiService yxOpenApiService;
	
	@Autowired
	@Qualifier("categoryService")
	private CategoryService categoryService;
	
	@GetMapping("/listFirstCategory")
	public ModelAndView listFirstCategory(HttpServletRequest req,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listFirstCategory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if(StringUtil.isNullOrEmpty(category.getParentId())){
				category.setParentId("0");
			}
			if(StringUtil.isNullOrEmpty(category.getCatPath())){
				category.setCatPath("1");
			}
			if(StringUtil.isNullOrEmpty(category.getEcomCode())){
				category.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());
			}
			PageInfo<GoodsCategory> pageList = categoryService.getCategoryPage(startNum, pageSize, category);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("category", category);
			mv.addObject("showList", GoodsCategoryListShowType.values());
		} catch (Exception e) {
			logger.error("## 查询商品一级分类列表信息出错", e);
		}
		return mv;
	}
	
	@PostMapping("/listFirstCategory")
	public ModelAndView listFirstCategory(HttpServletRequest req,HttpServletResponse resp,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listFirstCategory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
		PageInfo<GoodsCategory> pageList = categoryService.getCategoryPage(startNum, pageSize, category);
		mv.addObject("pageInfo", pageList);
		} catch(Exception e){
			e.printStackTrace();
		}
		mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
		mv.addObject("showList", GoodsCategoryListShowType.values());
		mv.addObject("category", category);
		return mv;
	}
	
	@GetMapping("/listSecondCateGory")
	public ModelAndView listSecondCateGory(HttpServletRequest req,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listSecondCateGory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if(StringUtil.isNullOrEmpty(category.getEcomCode())){
				category.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());
			}
			PageInfo<GoodsCategory> pageList = categoryService.getSecondCateGoryPage(startNum, pageSize, category);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("category", category);
			mv.addObject("showList", GoodsCategoryListShowType.values());
		} catch (Exception e) {
			logger.error("## 查询商品二级分类列表信息出错", e);
		}
		return mv;
	}
	
	@PostMapping("/listSecondCateGory")
	public ModelAndView listSecondCateGory(HttpServletRequest req,HttpServletResponse resp,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listSecondCateGory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
		PageInfo<GoodsCategory> pageList = categoryService.getSecondCateGoryPage(startNum, pageSize, category);
		mv.addObject("pageInfo", pageList);
		} catch(Exception e){
			e.printStackTrace();
		}
		mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
		mv.addObject("showList", GoodsCategoryListShowType.values());
		mv.addObject("category", category);
		return mv;
	}
	

	@PostMapping("/getCategory")
	public GoodsCategory getCategory(HttpServletRequest req){
		GoodsCategory category = null;
		try {
			String catId = req.getParameter("catId");
			category = categoryService.getGoodsCategoryByPrimaryKey(catId);
			category.setEcomType(GoodsEcomCodeType.findByCode(category.getEcomCode()).getValue());
		} catch (Exception e) {
			logger.error("## 查询商品分类明细出错", e);
		}
		
		return category;
	}
	
	@PostMapping("/commitEditCategory")
	public BaseResult<Object> commitEditCategory(HttpServletRequest req){
		try {
			String catId = req.getParameter("catId");
			String catSolr = req.getParameter("catSolr");
			String listShow = req.getParameter("listShow");
			GoodsCategory category = new GoodsCategory();
			category.setCatId(catId);
			category.setCatSolr(Long.valueOf(catSolr));
			category.setListShow(listShow);
			
			int i = categoryService.updateGoodsCategory(category);
			if(i!=1){
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			return ResultsUtil.success();
		} catch (Exception e) {
			logger.error("## 查询商品分类明细出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}
	
	@GetMapping("/listCategory")
	public ModelAndView listCategory(HttpServletRequest req,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listCateGory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		if(StringUtil.isNullOrEmpty(category.getEcomCode())){
			category.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());
		}
		PageInfo<GoodsCategory> pageList = categoryService.getGoodsCategoryListPage(startNum, pageSize, category);
		mv.addObject("pageInfo", pageList);
		mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
		mv.addObject("showList", GoodsCategoryListShowType.values());
		mv.addObject("category", category);
		return mv;
	}
	
	@PostMapping("/listCategory")
	public ModelAndView listCategory(HttpServletRequest req,HttpServletResponse resp,GoodsCategory category){
		ModelAndView mv = new ModelAndView("category/listCateGory");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
		PageInfo<GoodsCategory> pageList = categoryService.getGoodsCategoryListPage(startNum, pageSize, category);
		mv.addObject("pageInfo", pageList);
		} catch(Exception e){
			e.printStackTrace();
		}
		mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
		mv.addObject("showList", GoodsCategoryListShowType.values());
		mv.addObject("category", category);
		return mv;
	}
	
	
	@GetMapping("/syncGoodsCategory")
	public BaseResult<Object> syncGoodsCategory(HttpServletRequest req) {
		
		try {
			String ecomCode = req.getParameter("ecomCode");
			if (GoodsEcomCodeType.ECOM01.getCode().equals(ecomCode)) {
				JsonResult result = yxOpenApiService.handleGetCategory();
				if ("200".equals(result.getCode())) {
					List<APICategoryTO> list = JSONArray.parseArray(result.getResult().toString(), APICategoryTO.class);
					categoryService.saveGoodsCategory(list, ecomCode);
					return ResultsUtil.success(ecomCode);
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("## 同步商品分类出错", e);
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
	}
	
}
