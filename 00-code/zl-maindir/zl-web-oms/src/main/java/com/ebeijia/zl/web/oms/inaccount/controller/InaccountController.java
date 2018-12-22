package com.ebeijia.zl.web.oms.inaccount.controller;

import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.CheckStatEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.IsOpenEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TelRechargeConstants.providerDefaultRoute;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.ProviderInfFacade;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import com.ebeijia.zl.web.oms.providerChnl.service.ProviderInfService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "inaccount")
public class InaccountController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InaccountOrderService inaccountOrderService;

	@Autowired
	private InaccountOrderDetailService inaccountOrderDetailService;

	@RequestMapping(value = "/getInaccountByOrderId")
	@ResponseBody
	public ModelMap getInaccountByOrderId(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			InaccountOrder order  = inaccountOrderService.getInaccountOrderByOrderId(orderId);
			if (order != null) {
				resultMap.put("msg", order);
			}
		} catch (Exception e) {
			logger.error("## 查询订单{}异常", orderId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

	@RequestMapping(value = "/getInaccountByOrderListId")
	@ResponseBody
	public ModelMap getInaccountByOrderListId(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		InaccountOrderDetail orderDetail  = null;
		try {
			orderDetail = inaccountOrderDetailService.getById(orderListId);
			if (orderDetail != null) {
				orderDetail.setBName(SpecAccountTypeEnum.findByBId(orderDetail.getBId()).getName());
				orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBCentToYuan(orderDetail.getCompanyInAmt().toString())));
				resultMap.put("msg", orderDetail);
			}
		} catch (Exception e) {
			logger.error("## 查询订单明细{}异常", orderDetail, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "网络异常，请稍后再试");
			return resultMap;
		}
		return resultMap;
	}

}
