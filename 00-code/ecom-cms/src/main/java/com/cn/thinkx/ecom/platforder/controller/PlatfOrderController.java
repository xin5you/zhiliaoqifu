package com.cn.thinkx.ecom.platforder.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.ExcelUtil;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.platforder.domain.PlatfOrderUpload;
import com.cn.thinkx.ecom.platforder.domain.PlatfShopOrderUpload;
import com.cn.thinkx.ecom.platforder.service.PlatfOrderInfService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("platforder/platforder")
public class PlatfOrderController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderInfService platfOrderService;

	/**
	 * 查询电商平台一级订单信息
	 * 
	 * @param req
	 * @param platfOrder
	 * @return
	 */
	@RequestMapping(value = "/getPlatfOrderList")
	public ModelAndView getPlatfOrderList(HttpServletRequest req, PlatfOrder platfOrder) {
		ModelAndView mv = new ModelAndView("platforder/listPlatfOrder");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<PlatfOrder> pageInfo = null;
		try {
			pageInfo = platfOrderService.getPlatforderListPage(startNum, pageSize, platfOrder);
		} catch (Exception e) {
			logger.error("## 查询商城一级订单异常[{}]", e);
		}
		mv.addObject("payStatusList", Constants.PlatfOrderPayStat.values());
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("platfOrder", platfOrder);
		return mv;
	}

	/**
	 * 查询电商平台一级订单信息导出
	 * 
	 * @param req
	 * @param resp
	 * @param platfOrder
	 */
	@PostMapping(value = "/uploadListPlatfOrder")
	public void uploadListPlatfOrder(HttpServletRequest req, HttpServletResponse resp, PlatfOrder platfOrder) {
		try {
			List<PlatfOrder> platfOrderList = platfOrderService.getPlatfOrderList(platfOrder);
			List<PlatfOrderUpload> platfOrderUploadList = new ArrayList<PlatfOrderUpload>();
			for (PlatfOrder pso : platfOrderList) {
				PlatfOrderUpload pou = new PlatfOrderUpload();
				pou.setOrderId(pso.getOrderId());
				pou.setDmsRelatedKey(pso.getDmsRelatedKey());
				pou.setMemberId(pso.getMemberId());
				pou.setMobilePhoneNo(pso.getMobilePhoneNo());
				pou.setPersonalName(pso.getPersonalName());
				if (!StringUtil.isNullOrEmpty(pso.getOrderPrice()))
					pou.setOrderPrice(NumberUtils.RMBCentToYuan("" + pso.getOrderPrice()));
				if (!StringUtil.isNullOrEmpty(pso.getOrderFreightAmt()))
					pou.setOrderFreightAmt(NumberUtils.RMBCentToYuan("" + pso.getOrderFreightAmt()));
				pou.setPayType(pso.getPayType());
				if (!StringUtil.isNullOrEmpty(pso.getPayAmt()))
					pou.setPayAmt(NumberUtils.RMBCentToYuan("" + pso.getPayAmt()));
				pou.setPayStatus(Constants.PlatfOrderPayStat.findByCode(pso.getPayStatus()).getValue());
				pou.setCreateTime(pso.getCreateTime());
				pou.setPayTime(pso.getPayTime());
				platfOrderUploadList.add(pou);
			}
			String titlerow = "电商一级订单报表";
			String[] titlehead = new String[] { "一级订单编号", "外部交易流水", "会员ID", "会员手机号", "会员昵称", "订单金额(元)", "配送总费用(元)",
					"支付方式类型", "支付总额(元)", "订单状态", "下单时间", "支付时间", };
			ExcelUtil<PlatfOrderUpload> ex = new ExcelUtil<PlatfOrderUpload>();
			ex.upLoadExcel(titlerow, titlerow, platfOrder.getBeginTime(), platfOrder.getEndTime(), titlehead, null,
					null, platfOrderUploadList, PlatfOrderUpload.class, null, resp);
		} catch (Exception e) {
			logger.error("## 电商一级订单导出报表信息出错", e);
		}
	}

	/**
	 * 查询电商平台二级订单信息
	 * 
	 * @param req
	 * @param platfShopOrder
	 * @return
	 */
	@RequestMapping(value = "/getPlatfShopOrderList")
	public ModelAndView getPlatfShopOrderList(HttpServletRequest req, PlatfShopOrder platfShopOrder) {
		ModelAndView mv = new ModelAndView("platforder/listPlatfShopOrder");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		String orderId = req.getParameter("orderId");
		if (!StringUtil.isNullOrEmpty(platfShopOrder.getOrderId())) {
			orderId = platfShopOrder.getOrderId();
		}
		PageInfo<PlatfShopOrder> pageInfo = null;
		if (!StringUtil.isNullOrEmpty(orderId)) {
			platfShopOrder.setOrderId(orderId);
			try {
				pageInfo = platfOrderService.getPlatfShopOrderListPage(startNum, pageSize, platfShopOrder);
			} catch (Exception e) {
				logger.error("## 查询商城二级订单异常[{}]", e);
			}
		}
		mv.addObject("ecomCodeList", Constants.GoodsEcomCodeType.values());
		mv.addObject("subOrderStatusList", Constants.SubOrderStatus.values());
		mv.addObject("orderId", orderId);
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("platfShopOrder", platfShopOrder);
		return mv;
	}

	/**
	 * 电商二级订单查询列表
	 * 
	 * @param req
	 * @param platfShopOrder
	 * @return
	 */
	@RequestMapping(value = "/getPlatfShopOrderLists")
	public ModelAndView getPlatfShopOrderLists(HttpServletRequest req, PlatfShopOrder platfShopOrder) {
		ModelAndView mv = new ModelAndView("platforder/listPlatfShopOrders");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);

		PageInfo<PlatfShopOrder> pageInfo = null;
		try {
			pageInfo = platfOrderService.getPlatfShopOrderListsPage(startNum, pageSize, platfShopOrder);
		} catch (Exception e) {
			logger.error("## 查询商城二级订单异常[{}]", e);
		}
		mv.addObject("ecomCodeList", Constants.GoodsEcomCodeType.values());
		mv.addObject("subOrderStatusList", Constants.SubOrderStatus.values());
		mv.addObject("payStatusList", Constants.PlatfOrderPayStat.values());
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("platfShopOrder", platfShopOrder);
		return mv;
	}

	/**
	 * 电商二级订单查询列表导出
	 * 
	 * @param req
	 * @param resp
	 * @param platfShopOrder
	 */
	@PostMapping(value = "/uploadListPlatfShopOrders")
	public void uploadListPlatfShopOrders(HttpServletRequest req, HttpServletResponse resp,
			PlatfShopOrder platfShopOrder) {
		try {
			List<PlatfShopOrder> platfShopOrderList = platfOrderService.getPlatfShopOrderLists(platfShopOrder);
			List<PlatfShopOrderUpload> platfShopOrderUploadList = new ArrayList<PlatfShopOrderUpload>();
			for (PlatfShopOrder pso : platfShopOrderList) {
				PlatfShopOrderUpload psou = new PlatfShopOrderUpload();
				psou.setSOrderId(pso.getsOrderId());
				psou.setOrderId(pso.getOrderId());
				psou.setMemberId(pso.getMemberId());
				psou.setPersonalName(pso.getPersonalName());
				psou.setMobilePhoneNo(pso.getMobilePhoneNo());
				psou.setEcomCode(Constants.GoodsEcomCodeType.findByCode(pso.getEcomCode()).getValue());
				psou.setPayAmt(StringUtil.isNullOrEmpty(pso.getPayAmt())? "0.00" : NumberUtils.RMBCentToYuan(pso.getPayAmt()));
				psou.setShippingFreightPrice(StringUtil.isNullOrEmpty(pso.getShippingFreightPrice())? "0.00" : NumberUtils.RMBCentToYuan(pso.getShippingFreightPrice()));
				psou.setChnlOrderPrice(StringUtil.isNullOrEmpty(pso.getChnlOrderPrice())? "0.00" : NumberUtils.RMBCentToYuan(pso.getChnlOrderPrice()));
				psou.setChnlOrderPostage(StringUtil.isNullOrEmpty(pso.getChnlOrderPostage())? "0.00" : NumberUtils.RMBCentToYuan(pso.getChnlOrderPostage()));
				psou.setSubOrderStatusValue(Constants.SubOrderStatus.findByCode(pso.getSubOrderStatus()).getValue());
				psou.setPayStatus(Constants.PlatfOrderPayStat.findByCode(pso.getPayStatus()).getValue());
				psou.setCreateTime(pso.getCreateTime());
				psou.setPayStatus(Constants.PlatfOrderPayStat.findByCode(pso.getPayStatus()).getValue());
				psou.setPayTime(pso.getPayTime());
				platfShopOrderUploadList.add(psou);
			}
			String titlerow = "电商二级订单报表";
			String[] titlehead = new String[] { "二级订单编号", "一级订单编号", "会员ID", "会员昵称", "会员手机号", "电商名称", "订单金额(元)",
					"配送费用(元)","渠道订单金额(元)","渠道订单邮费(元)", "订单状态", "下单时间", "支付状态", "支付时间", };
			ExcelUtil<PlatfShopOrderUpload> ex = new ExcelUtil<PlatfShopOrderUpload>();
			ex.upLoadExcel(titlerow, titlerow, platfShopOrder.getBeginTime(), platfShopOrder.getEndTime(), titlehead,
					null, null, platfShopOrderUploadList, PlatfShopOrderUpload.class, null, resp);
		} catch (Exception e) {
			logger.error("## 电商二级订单导出报表信息出错", e);
		}
	}

	/**
	 * 重新下单
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/placeOrder")
	public BaseResult<Object> placeOrder(HttpServletRequest req) {
		String sOrderId = req.getParameter("sOrderId");
		return platfOrderService.placeOrder(sOrderId);
	}

	/**
	 * 订单作废
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/cancellationOrder")
	public BaseResult<Object> cancellationOrder(HttpServletRequest req) {
		String sOrderId = req.getParameter("sOrderId");
		return platfOrderService.cancellationOrder(sOrderId);
	}
}