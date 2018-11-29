package com.cn.thinkx.ecom.inventory.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsMarketType;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.inventory.service.InventoryService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "ecom/inventory")
public class InventoryController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping("/listInventory")
	public ModelAndView listInventory(HttpServletRequest req, GoodsProduct pro){
		ModelAndView mv = new ModelAndView("inventory/listInventory");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if(StringUtil.isNullOrEmpty(pro.getEcomCode())){
				pro.setEcomCode(GoodsEcomCodeType.ECOM01.getCode());//
			}
			PageInfo<GoodsProduct> pageList = inventoryService.getInventoryPage(startNum, pageSize, pro);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("marketList", GoodsMarketType.values());
			mv.addObject("pro", pro);
		} catch (Exception e) {
			logger.error("## 查询商品库存出错", e);
		}
		
		return mv;
	}
	
	@PostMapping("/listInventory")
	public ModelAndView listInventory(HttpServletRequest req,HttpServletResponse resp, GoodsProduct pro){
		ModelAndView mv = new ModelAndView("inventory/listInventory");
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<GoodsProduct> pageList = inventoryService.getInventoryPage(startNum, pageSize, pro);
			mv.addObject("pageInfo", pageList);
			mv.addObject("ecomTypeList",GoodsEcomCodeType.values());
			mv.addObject("marketList", GoodsMarketType.values());
			mv.addObject("pro", pro);
		} catch (Exception e) {
			logger.error("## 查询商品库存出错", e);
		}
		
		return mv;
	}

}
