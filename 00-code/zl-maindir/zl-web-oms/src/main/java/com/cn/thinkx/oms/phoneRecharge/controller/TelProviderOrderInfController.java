package com.cn.thinkx.oms.phoneRecharge.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cn.thinkx.wecard.facade.telrecharge.model.TelProviderOrderInf;
import com.cn.thinkx.wecard.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.providerOrderRechargeState;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "provider/providerOrder")
public class TelProviderOrderInfController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Reference(check=false)
	private ProviderOrderInfFacade telProviderOrderInfFacade;

	/**
	 * 供应商订单列表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listTelProviderOrderInf")
	public ModelAndView listTelProviderOrderInf(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("phoneRecharge/telProviderOrderInf/listTelProviderOrderInf");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			TelProviderOrderInf telProviderOrderInf = this.getTelProviderOrderInf(req);
			PageInfo<TelProviderOrderInf> pageList = telProviderOrderInfFacade.getTelProviderOrderInfPage(startNum,
					pageSize, telProviderOrderInf);
			mv.addObject("pageInfo", pageList);
			mv.addObject("telProviderOrderInf", telProviderOrderInf);
		} catch (Exception e) {
			logger.error("## 供应商订单列表查询异常", e);
		}
		mv.addObject("rechargeStateList", providerOrderRechargeState.values());
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	/**
	 * 封装供应商订单
	 * 
	 * @param req
	 * @return
	 */
	public TelProviderOrderInf getTelProviderOrderInf(HttpServletRequest req) {
		TelProviderOrderInf telProviderOrderInf = new TelProviderOrderInf();
		telProviderOrderInf.setRegOrderId(StringUtil.nullToString(req.getParameter("regOrderId")));
		telProviderOrderInf.setChannelOrderId(StringUtil.nullToString(req.getParameter("channelOrderId")));
		telProviderOrderInf.setBillId(StringUtil.nullToString(req.getParameter("billId")));
		return telProviderOrderInf;
	}
}
