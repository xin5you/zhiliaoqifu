package com.cn.thinkx.ecom.refund.controller;

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

import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.common.constants.Constants.RefundStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsStatus;
import com.cn.thinkx.ecom.common.constants.Constants.ReturnsType;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.refund.service.RefundService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "ecom/refund")
public class RefundController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RefundService refundService;

	@GetMapping("/listRefundsOrder")
	public ModelAndView listRefundsOrder(HttpServletRequest req, ReturnsOrder refund) {
		ModelAndView mv = new ModelAndView("refund/listRefundsOrder");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<ReturnsOrder> pageList = refundService.getRefundsOrderPage(startNum, pageSize, refund);
			mv.addObject("pageInfo", pageList);
			mv.addObject("refund", refund);
			mv.addObject("returnsStatusList", ReturnsStatus.values());
			mv.addObject("returnsTypeList", ReturnsType.values());
			mv.addObject("refundStatusList", RefundStatus.values());
		} catch (Exception e) {
			logger.error("## 查询退款申请列表信息出错", e);
		}
		return mv;
	}

	@PostMapping("/listRefundsOrder")
	public ModelAndView listRefundsOrder(HttpServletRequest req, HttpServletResponse resp, ReturnsOrder refund) {
		ModelAndView mv = new ModelAndView("refund/listRefundsOrder");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<ReturnsOrder> pageList = refundService.getRefundsOrderPage(startNum, pageSize, refund);
			mv.addObject("pageInfo", pageList);
			mv.addObject("refund", refund);
			mv.addObject("returnsStatusList", ReturnsStatus.values());
			mv.addObject("returnsTypeList", ReturnsType.values());
			mv.addObject("refundStatusList", RefundStatus.values());
		} catch (Exception e) {
			logger.error("## 查询退款申请列表信息出错", e);
		}
		return mv;
	}

	// @PostMapping("/findReturnsOrder")
	// public ReturnsOrder findReturnsOrder(HttpServletRequest req
	// ,HttpServletResponse resp){
	// String returnsId = req.getParameter("returnsId");
	// ReturnsOrder returnsOrder = null;
	// try {
	// returnsOrder = refundService.getReturnsOrderByReturnsId(returnsId);
	// returnsOrder.setRetStatType(ReturnsStatus.findByCode(returnsOrder.getReturnsStatus()).getValue());
	// returnsOrder.setRetType(ReturnsType.findByCode(returnsOrder.getReturnsType()).getValue());
	// returnsOrder.setRefStatType(RefundStatus.findByCode(returnsOrder.getRefundStatus()).getValue());
	// returnsOrder.setRefundAmt(NumberUtils.RMBCentToYuan(returnsOrder.getRefundAmt()));
	// } catch (Exception e) {
	// logger.error("## 查询退换货申请id为[{}]的退化货数据出错", returnsId,e);
	// }
	// return returnsOrder;
	// }

	@PostMapping("/refundCommit")
	public BaseResult<Object> refundCommit(HttpServletRequest req, HttpServletResponse resp) {
		return refundService.updateRefund(req, resp);
	}
}
