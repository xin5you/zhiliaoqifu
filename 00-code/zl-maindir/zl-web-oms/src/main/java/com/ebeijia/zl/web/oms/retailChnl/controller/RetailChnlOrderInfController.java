package com.ebeijia.zl.web.oms.retailChnl.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelOrderNotifyStat;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelOrderStat;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ShopType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlInfService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "retailChnl/retailChnlOrder")
public class RetailChnlOrderInfController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RetailChnlOrderInfFacade retailChnlOrderInfFacade;

	@Autowired
	private RetailChnlInfService retailChnlInfService;

	/**
	 * 分销商订单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listRetailChnlOrderInf")
	public ModelAndView listRetailChnlOrderInf(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlOrderInf/listRetailChnlOrderInf");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);
		RetailChnlOrderInf orderInf = new RetailChnlOrderInf();
		orderInf.setChannelName(StringUtil.nullToString(request.getParameter("channelName")));
		orderInf.setChannelOrderId(StringUtil.nullToString(request.getParameter("channelOrderId")));
		orderInf.setOuterTid(StringUtil.nullToString(request.getParameter("outerTid")));
		orderInf.setRechargeType(StringUtil.nullToString(request.getParameter("rechargeType")));
		orderInf.setOrderStat(StringUtil.nullToString(request.getParameter("orderStat")));
		orderInf.setNotifyStat(StringUtil.nullToString(request.getParameter("notifyStat")));
		try {
			PageInfo<RetailChnlOrderInf> pageList = retailChnlOrderInfFacade.getRetailChnlOrderInfPage(startNum, pageSize, orderInf);
			mv.addObject("pageInfo", pageList);
		} catch (Exception e) {
			logger.error("## 分销商订单查询异常", e);
		}
		mv.addObject("retailChnlOrderInf", orderInf);
		mv.addObject("rechargeTypeList", ShopType.values());
		mv.addObject("channelOrderStatList", ChannelOrderStat.values());
		mv.addObject("channelOrderNotifyStatList", ChannelOrderNotifyStat.values());
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	@RequestMapping(value = "/doCallBackNotifyChannel")
	@ResponseBody
	public ModelMap doCallBackNotifyChannel(HttpServletRequest req) {
		ModelMap resultMap = new ModelMap();
		String channelOrderId = req.getParameter("channelOrderId");
		try {
			resultMap = retailChnlInfService.doCallBackNotifyChannel(channelOrderId);
		} catch (Exception e) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "回调通知分销商异常,请联系管理员");
			logger.error("## 回调通知分销商异常", e);
		}
		return resultMap;
	}
}
