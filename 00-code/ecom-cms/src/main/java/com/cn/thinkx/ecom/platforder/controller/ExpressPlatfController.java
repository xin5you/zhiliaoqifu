package com.cn.thinkx.ecom.platforder.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.platforder.service.ExpressPlatfInfService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("platforder/expressPlatf")
public class ExpressPlatfController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExpressPlatfInfService expressPlatfService;

	@RequestMapping("/getExpressPlatfList")
	public ModelAndView getExpressPlatfList(HttpServletRequest req, ExpressPlatf expressPlatf) {
		ModelAndView mv = new ModelAndView("platforder/listExpressPlatf");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<ExpressPlatf> pageInfo = null;
		try {
			pageInfo = expressPlatfService.getExpressPlatfListPage(startNum, pageSize, expressPlatf);
		} catch (Exception e) {
			logger.error("## 查询订单的物流信息异常，[{}]", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("expressPlatf", expressPlatf);
		mv.addObject("ecomCodeTypeList", Constants.GoodsEcomCodeType.values());
		return mv;
	}
	
	@RequestMapping("/getExpressPlatfProductListByPackId")
	public ModelAndView getExpressPlatfProductListByPackId(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("platforder/listExpressPlatfProduct");
		String packId = req.getParameter("packId");
		if(StringUtil.isNullOrEmpty(packId)){
			logger.error("## 查询包裹货品失败,包裹号为空：[{}]",packId);
		}
		List<ExpressPlatf> expressPlatfProList =  expressPlatfService.getExpressPlatfProductByPackId(packId);
		for (ExpressPlatf expressPlatf : expressPlatfProList) {
			if(!StringUtil.isNullOrEmpty(expressPlatf.getGoodsPrice()))
				expressPlatf.setGoodsPrice(NumberUtils.RMBCentToYuan("" + expressPlatf.getGoodsPrice()));
		}
		mv.addObject("expressPlatfProList", expressPlatfProList);
		return mv;
	}
}
