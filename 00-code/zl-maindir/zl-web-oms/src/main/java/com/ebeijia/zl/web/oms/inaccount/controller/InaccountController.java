package com.ebeijia.zl.web.oms.inaccount.controller;

import com.ebeijia.zl.common.utils.enums.IsInvoiceEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> getInaccountByOrderId(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
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
	public Map<String, Object> getInaccountByOrderListId(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderListId = StringUtil.nullToString(req.getParameter("orderListId"));
		InaccountOrderDetail orderDetail  = null;
		try {
			orderDetail = inaccountOrderDetailService.getById(orderListId);
			if (orderDetail != null) {
				orderDetail.setBName(SpecAccountTypeEnum.findByBId(orderDetail.getBId()).getName());
				orderDetail.setCompanyInAmt(new BigDecimal(NumberUtils.RMBCentToYuan(orderDetail.getCompanyInAmt().toString())));
				orderDetail.setInaccountAmt(new BigDecimal(NumberUtils.RMBCentToYuan(orderDetail.getInaccountAmt().toString())));
				orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBCentToYuan(orderDetail.getTransAmt().toString())));
				orderDetail.setPlatformInAmt(new BigDecimal(NumberUtils.RMBCentToYuan(orderDetail.getPlatformInAmt().toString())));
				orderDetail.setIsInvoice(IsInvoiceEnum.findByBId(orderDetail.getIsInvoice()).getName());
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
