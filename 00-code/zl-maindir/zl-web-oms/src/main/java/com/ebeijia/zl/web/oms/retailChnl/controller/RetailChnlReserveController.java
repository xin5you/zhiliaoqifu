package com.ebeijia.zl.web.oms.retailChnl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelReserveType;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;

@Controller
@RequestMapping(value = "retailChnl/reserve")
public class RetailChnlReserveController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RetailChnlInfFacade retailChnlInfFacade;

	/**
	 * 分销商备付金列表
	 * 
	 * @param request
	 * @return
	 */
	/*@RequestMapping("/listRetailChnlReserve")
	public ModelAndView listTelChannelReserve(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlReserve/listRetailChnlReserve");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);

		RetailChnlReserveDetail reserve = new RetailChnlReserveDetail();
		reserve.setChannelId(StringUtil.nullToString(request.getParameter("channelId")));
		reserve.setChannelName(StringUtil.nullToString(request.getParameter("channelName")));
		PageInfo<RetailChnlReserveDetail> pageList = null;
		try {
			pageList = retailChnlReserveDetailFacade.getRetailChnlReserveDetailPage(startNum, pageSize, reserve);
		} catch (Exception e) {
			logger.error("## 查询分销商备付金列表出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("reserve", reserve);
		mv.addObject("operStatus", operStatus);
		return mv;
	}*/

	@RequestMapping(value = "/intoAddRetailChnlReserve")
	public ModelAndView intoAddTelChannelReserve(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlReserve/addRetailChnlReserve");
		List<RetailChnlInf> list = null;
		try {
			list = retailChnlInfFacade.getRetailChnlInfList(null);
		} catch (Exception e) {
			logger.error("## 查询分销商列表出错", e);
		}
		mv.addObject("channelInfList", list);
		mv.addObject("reserveTypeList", ChannelReserveType.values());
		return mv;
	}

	/*@RequestMapping(value = "/addRetailChnlReserveCommit")
	public ModelAndView addTelChannelReserveCommit(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("redirect:/retailChnl/retailChnlReserve/listRetailChnlReserve.do");
		String channelId = req.getParameter("channelId");
		String reserveAmt = req.getParameter("reserveAmt");
		String reserveType = req.getParameter("reserveType");
		String remarks = req.getParameter("remarks");
		RetailChnlReserveDetail reserve = new RetailChnlReserveDetail();
		reserve.setChannelId(channelId);
		reserve.setReserveAmt(new BigDecimal(reserveAmt));
		reserve.setReserveType(reserveType);
		reserve.setRemarks(remarks);
		reserve.setId(IdUtil.getNextId());
		try {
			boolean flag = retailChnlReserveDetailFacade.updateRetailChnlInfReserve(reserve);
			if (flag) {
				mv.addObject("operStatus", 1);
			}
		} catch (Exception e) {
			logger.error(" ## 添加商品备付金出错", e);
		}
		return mv;
	}*/

	/*@RequestMapping(value = "/intoViewRetailChnlReserve")
	public ModelAndView intoViewTelChannelReserve(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("retailChnl/retailChnlReserve/viewRetailChnlReserve");
		String reserveId = StringUtil.nullToString(req.getParameter("reserveId"));
		RetailChnlReserveDetail reserve = null;
		try {
			reserve = retailChnlReserveDetailFacade.getRetailChnlReserveDetailById(reserveId);
			reserve.setReserveType(ChannelReserveType.findByCode(reserve.getReserveType()));
		} catch (Exception e) {
			logger.error("## 查询分销商备付金明细出错", e);
		}
		mv.addObject("reserve", reserve);
		return mv;
	}*/
}
