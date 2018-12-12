package com.ebeijia.zl.web.oms.phoneRecharge.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.ChannelReserveType;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;

@Controller
@RequestMapping(value = "channel/reserve")
public class TelChannelReserveController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Reference(check=false)
	private RetailChnlInfFacade telChannelInfFacade;

	@Reference(check=false)
	private CompanyInfFacade telChannelReserveDetailFacade;

	/**
	 * 分销商备付金列表
	 * 
	 * @param request
	 * @return
	 */
	/*@RequestMapping("/listTelChannelReserve")
	public ModelAndView listTelChannelReserve(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelReserve/listTelChannelReserve");
		String operStatus = StringUtil.nullToString(request.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(request.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(request.getParameter("pageSize"), 10);

		TelChannelReserveDetail reserve = new TelChannelReserveDetail();
		reserve.setChannelId(StringUtil.nullToString(request.getParameter("channelId")));
		reserve.setChannelName(StringUtil.nullToString(request.getParameter("channelName")));
		PageInfo<TelChannelReserveDetail> pageList = null;
		try {
			pageList = telChannelReserveDetailFacade.getTelChannelReserveDetailPage(startNum, pageSize, reserve);
		} catch (Exception e) {
			logger.error("## 查询分销商备付金列表出错", e);
		}
		mv.addObject("pageInfo", pageList);
		mv.addObject("reserve", reserve);
		mv.addObject("operStatus", operStatus);
		return mv;
	}*/

	@RequestMapping(value = "/intoAddTelChannelReserve")
	public ModelAndView intoAddTelChannelReserve(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelReserve/addTelChannelReserve");
		List<RetailChnlInf> list = null;
		try {
			list = telChannelInfFacade.getRetailChnlInfList(null);
		} catch (Exception e) {
			logger.error("## 查询分销商列表出错", e);
		}
		mv.addObject("channelInfList", list);
		mv.addObject("reserveTypeList", ChannelReserveType.values());
		return mv;
	}

	/*@RequestMapping(value = "/addTelChannelReserveCommit")
	public ModelAndView addTelChannelReserveCommit(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("redirect:/channel/reserve/listTelChannelReserve.do");
		String channelId = req.getParameter("channelId");
		String reserveAmt = req.getParameter("reserveAmt");
		String reserveType = req.getParameter("reserveType");
		String remarks = req.getParameter("remarks");
		TelChannelReserveDetail reserve = new TelChannelReserveDetail();
		reserve.setChannelId(channelId);
		reserve.setReserveAmt(new BigDecimal(reserveAmt));
		reserve.setReserveType(reserveType);
		reserve.setRemarks(remarks);
		reserve.setId(UUID.randomUUID().toString());
		try {
			boolean flag = telChannelReserveDetailFacade.updateTelChannelInfReserve(reserve);
			if (flag) {
				mv.addObject("operStatus", 1);
			}
		} catch (Exception e) {
			logger.error(" ## 添加商品备付金出错", e);
		}
		return mv;
	}*/

	/*@RequestMapping(value = "/intoViewTelChannelReserve")
	public ModelAndView intoViewTelChannelReserve(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telChannelReserve/viewTelChannelReserve");
		String reserveId = StringUtil.nullToString(req.getParameter("reserveId"));
		TelChannelReserveDetail reserve = null;
		try {
			reserve = telChannelReserveDetailFacade.getTelChannelReserveDetailById(reserveId);
			reserve.setReserveType(ChannelReserveType.findByCode(reserve.getReserveType()));
		} catch (Exception e) {
			logger.error("## 查询分销商备付金明细出错", e);
		}
		mv.addObject("reserve", reserve);
		return mv;
	}*/
}
