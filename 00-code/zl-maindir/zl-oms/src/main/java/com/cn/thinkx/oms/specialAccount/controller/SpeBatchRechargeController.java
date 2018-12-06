/*package com.cn.thinkx.oms.specialAccount.controller;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.util.OrderConstants;
import com.cn.thinkx.oms.specialAccount.util.PagePersonUtil;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.cn.thinkx.pms.base.utils.BaseConstants.OMSOrderBizType;
import com.cn.thinkx.pms.base.utils.BaseConstants.OMSOrderStat;
import com.cn.thinkx.pms.base.utils.BaseConstants.OMSOrderType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "speaccount/batchRecharge")
public class SpeBatchRechargeController {

	Logger logger = LoggerFactory.getLogger(SpeBatchRechargeController.class);

	@Autowired
	private SpeAccountBatchOrderService speAccountBatchOrderService;
	
	@Autowired
	private SpeAccountBatchOrderListService speAccountBatchOrderListService;
	
	@Autowired
	private BillingTypeInfService billingTypeInfService;

	*//**
	 * 批量充值列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/listRecharge")
	public ModelAndView listRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchRecharge/listRecharge");
		String operStatus = StringUtils.nullToString(req.getParameter("operStatus"));
		PageInfo<SpeAccountBatchOrder> pageList = null;
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderType(OMSOrderType.orderType_300.getCode());
		try {
			pageList = speAccountBatchOrderService.getSpeAccountBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 查询列表信息出错", e);
		}
		mv.addObject("order", order);
		mv.addObject("mapOrderStat", OMSOrderStat.values());
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
//		mv.addObject("productList", productList);
		mv.addObject("rechargeTypeList", BaseConstants.OMSOrderBizType.values());
		return mv;
	}

	*//**
	 * 新增批量充值
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoAddRecharge")
	public ModelAndView intoAddRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchRecharge/addRecharge");
		BigDecimal sumMoney = BigDecimal.ZERO;
//		List<Product> productList = productService.getProductList(null);
		BillingTypeInf billing = new BillingTypeInf();
		billing.setType("A");
		List<BillingTypeInf> billingList = billingTypeInfService.getBillingTypeInfList(billing);
		LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathRechargeSession);
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		Page<SpeAccountBatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null) {
//			orderList.forEach(batchOrderList ->{
//				sumMoney = sumMoney.add(BigDecimal.valueOf(Double.valueOf(batchOrderList.getAmount())));
//			});
			double sum = orderList.stream().mapToDouble(o -> Double.valueOf(o.getAmount())).summaryStatistics().getSum();
			sumMoney = sumMoney.add(BigDecimal.valueOf(sum));
//			for (SpeAccountBatchOrderList batchOrderList : orderList) {
//				sumMoney = sumMoney.add(BigDecimal.valueOf(Double.valueOf(batchOrderList.getAmount())));
//			}
			page.setTotal(orderList.size());
			List<SpeAccountBatchOrderList> list = PagePersonUtil.getPersonInfPageList(startNum, pageSize, orderList);
			list.forEach(o ->{
				page.add(o);
			});
//			for (SpeAccountBatchOrderList o : list) {
//				page.add(o);
//			}
		} else {
			page.setTotal(0);
		}
		PageInfo<SpeAccountBatchOrderList> pageList = new PageInfo<SpeAccountBatchOrderList>(page);
		mv.addObject("pageInfo", pageList);
		mv.addObject("count", pageList.getTotal());
		mv.addObject("sumMoney", sumMoney.doubleValue());
		mv.addObject("billingList", billingList);
		mv.addObject("rechargeTypeList", OMSOrderBizType.values());
		return mv;
	}

	*//**
	 * 批量充值提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addRechargeCommit")
	@ResponseBody
	public ModelMap addRechargeCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		int i = 0;
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathRechargeSession);
		if (orderList == null) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		String accountType = req.getParameter("accountType");
		User user = this.getCurrUser(req);
		order.setCompanyName(StringUtils.nullToString(req.getParameter("companyName")));
		order.setBizType(StringUtils.nullToString(req.getParameter("bizType")));
		order.setOrderName(StringUtils.nullToString(req.getParameter("orderName")));
		order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_10.getCode());
		order.setOrderType(BaseConstants.OMSOrderType.orderType_300.getCode());
		order.setAccountType(accountType);
//		Product product = productService.getProductByProductCode(productCode);
//		order.setProductName(product.getProductName());
//		order.setProductCode(productCode);
		order.setCreateUser(user.getId().toString());
		order.setUpdateUser(user.getId().toString());
		try {
			i = speAccountBatchOrderService.addSpeAccountBatchOrder(order, orderList);
			if (i > 0) {
				JedisClusterUtils.getInstance().del(OrderConstants.speBathRechargeSession);
			}
		} catch (Exception e) {
			logger.error("新增出错----->>[{}]", e.getMessage());
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "新增失败，请重新添加");
		}
		return resultMap;
	}

	*//**
	 * 批量充值订单详情
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoViewRecharge")
	public ModelAndView intoViewRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchRecharge/viewRecharge");
		String orderId = req.getParameter("orderId");
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BaseConstants.OMSOrderStat.findStat(order.getOrderStat()));
		order.setBizType(order.getBizType()==null?"":BaseConstants.OMSOrderBizType.findType(order.getBizType()));
		if (order.getSumAmount() == null || "".equals(order.getSumAmount())) {
			order.setSumAmount(NumberUtils.RMBCentToYuan("0"));
		} else {
			order.setSumAmount(NumberUtils.RMBCentToYuan(order.getSumAmount()));
		}
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		return mv;
	}

	*//**
	 * 进入批量充值订单编辑
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoEditRecharge")
	public ModelAndView intoEditRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchRecharge/editRecharge");
		String orderId = req.getParameter("orderId");
		String operStatus = StringUtils.nullToString(req.getParameter("operStatus"));
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BaseConstants.OMSOrderStat.findStat(order.getOrderStat()));
		if (order.getSumAmount() == null || "".equals(order.getSumAmount())) {
			order.setSumAmount(NumberUtils.RMBCentToYuan("0"));
		} else {
			order.setSumAmount(NumberUtils.RMBCentToYuan(order.getSumAmount()));
		}
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	*//**
	 * 删除批量充值订单
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/deleteRechargeCommit")
	@ResponseBody
	public ModelMap deleteRechargeCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap map = new ModelMap();
		map.put("status", Boolean.TRUE);
		String orderId = StringUtils.nullToString(req.getParameter("orderId"));
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_19.getCode());
			speAccountBatchOrderService.updateSpeAccountBatchOrder(order);
		} catch (Exception e) {
			map.put("status", Boolean.FALSE);
			logger.error("## 删除批量充值订单,订单号：[{}]", orderId, e);
		}
		return map;
	}

	*//**
	 * 提交批量充值订单
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addOrderCommit")
	@ResponseBody
	public ModelMap addOrderCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtils.nullToString(req.getParameter("orderId"));
		User user = this.getCurrUser(req);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BaseConstants.OMSOrderStat.orderStat_10.getCode().equals(order.getOrderStat())) {
				speAccountBatchOrderService.batchSpeAccountRechargeITF(orderId, user, BaseConstants.OMSOrderStat.orderStat_10.getCode());
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 提交批量充值订单，订单号：[{}]，出错", orderId, e);
		}
		return resultMap;
	}

	*//**
	 * 重复提交批量充值订单
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addOrderAgainCommit")
	@ResponseBody
	public ModelMap addOrderAgainCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtils.nullToString(req.getParameter("orderId"));
		User user = this.getCurrUser(req);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BaseConstants.OMSOrderStat.orderStat_90.getCode().equals(order.getOrderStat())) {
				speAccountBatchOrderService.batchSpeAccountRechargeITF(orderId, user, BaseConstants.OMSOrderStat.orderStat_90.getCode());
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 重复提交批量充值订单，订单号：[{}],出错[{}]", orderId, e);
		}
		return resultMap;
	}

	*//**
	 * 批量充值添加充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addOrderListCommit")
	@ResponseBody
	public ModelMap addOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtils.nullToString(req.getParameter("orderId"));
		User user = this.getCurrUser(req);
		try {
//			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			orderList.setOrderId(orderId);
			orderList.setUserName(StringUtils.nullToString(req.getParameter("name")));
			orderList.setPhoneNo(StringUtils.nullToString(req.getParameter("phone")));
			orderList.setUserCardNo(StringUtils.nullToString(req.getParameter("card")));
			orderList.setCompanydCode(StringUtils.nullToString(req.getParameter("companydCode")));
			orderList.setAmount(NumberUtils.RMBYuanToCent(StringUtils.nullToString(req.getParameter("money"))));
//			orderList.setProductCode(order.getProductCode());
//			orderList.setProductName(order.getProductName());
			orderList.setOrderStat(BaseConstants.OMSOrderListStat.orderListStat_0.getCode());
			orderList.setCreateUser(user.getId().toString());
			orderList.setUpdateUser(user.getId().toString());
			SpeAccountBatchOrderList orderList2 = speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrderIdAndPhoneNo(orderList);
			if (orderList2 != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "电话号码重复！！！");
				return resultMap;
			}
			speAccountBatchOrderListService.addOrderList(orderList);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量充值添加充值信息出错", e);
		}
		return resultMap;
	}

	*//**
	 * 删除批量充值的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/deleteOrderListCommit")
	@ResponseBody
	public ModelMap deleteOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		try {
			speAccountBatchOrderListService.deleteSpeAccountBatchOrderList(req);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 删除批量充值信息出错", e);
		}
		return resultMap;
	}

	*//**
	 * 添加充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addAccountInf")
	@ResponseBody
	public ModelMap addAccountInf(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		try {
			String phone = StringUtils.nullToString(req.getParameter("phone"));
			String money = StringUtils.nullToString(req.getParameter("money"));
			SpeAccountBatchOrderList order = new SpeAccountBatchOrderList();
			order.setPuid(UUID.randomUUID().toString().replace("-", ""));
			order.setUserName(StringUtils.nullToString(req.getParameter("name")));
			order.setUserCardNo(StringUtils.nullToString(req.getParameter("card")));
			order.setPhoneNo(phone);
			order.setCompanydCode(StringUtils.nullToString(req.getParameter("companydCode")));
			order.setAmount(NumberUtils.RMBCentToYuan(NumberUtils.RMBYuanToCent(money)));
			LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathRechargeSession);
			if (orderList == null) {
				orderList = new LinkedList<SpeAccountBatchOrderList>();
			}
			orderList.forEach(o ->{
				if (o.getPhoneNo().equals(phone)) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "电话号码重复！！！");
					return;
				}
			});
			
//			for (SpeAccountBatchOrderList batchOrderList : orderList) {
//				if (batchOrderList.getPhoneNo().equals(phone)) {
//					resultMap.put("status", Boolean.FALSE);
//					resultMap.put("msg", "电话号码重复！！！");
//					return resultMap;
//				}
//			}
			orderList.addFirst(order);
			JedisClusterUtils.getInstance().setex(OrderConstants.speBathRechargeSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 添加充值用户信息出错", e);
		}
		return resultMap;
	}

	*//**
	 * 删除批量充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/deleteAccountInf")
	@ResponseBody
	public ModelMap deleteAccountInf(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		try {
			String puid = req.getParameter("puid");
			LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathRechargeSession);
			orderList.forEach(order ->{
				if (order.getPuid().equals(puid)) {
					orderList.remove(order);
					return;
				}
			});
//			for (SpeAccountBatchOrderList order : orderList) {
//				if (order.getPuid().equals(puid)) {
//					orderList.remove(order);
//					break;
//				}
//			}
			JedisClusterUtils.getInstance().setex(OrderConstants.speBathRechargeSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 删除批量充值用户信息", e);
		}
		return resultMap;
	}

}
*/