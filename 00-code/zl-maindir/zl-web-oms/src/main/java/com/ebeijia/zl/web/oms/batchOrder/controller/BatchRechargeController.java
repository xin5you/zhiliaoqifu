package com.ebeijia.zl.web.oms.batchOrder.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.common.util.PagePersonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrder;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderListService;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "batch/recharge")
public class BatchRechargeController {

	Logger logger = LoggerFactory.getLogger(BatchRechargeController.class);

	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	private BatchOrderListService batchOrderListService;
	
	@Autowired
	private BillingTypeService billingTypeInfService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
	@Autowired
	private CompanyInfFacade companyInfFacade;

	/**
	 * 批量充值列表
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listRecharge")
	public ModelAndView listRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/recharge/listRecharge");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);		
		
		BatchOrder order = new BatchOrder();
		order.setOrderType(TransCode.MB50.getCode());

		PageInfo<BatchOrder> pageList = null;
		try {
			pageList = batchOrderService.getBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 查询批量充值列表信息出错", e);
		}

		//查询已开户企业
		CompanyInf company = new CompanyInf();
		company.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
		List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(company);
		companyList = companyList.stream().filter(c -> c.getIsPlatform().equals(IsPlatformEnum.ISOPEN_FALSE.getCode())).collect(Collectors.toList());
		
		/*SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!StringUtil.isNullOrEmpty(order.getStartTime())) {
			String startTime = sdfLong.format(new Date(order.getStartTime()));
			order.setStartTime(startTime);//开始时间
		}
		if (!StringUtil.isNullOrEmpty(order.getEndTime())) {
			String endTime = sdfLong.format(new Date(order.getEndTime()));
			order.setEndTime(endTime);//结束时间
		}*/
		mv.addObject("order", order);
		mv.addObject("mapOrderStat", BatchOrderStat.values());
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("companyList", companyList);
		return mv;
	}

	/**
	 * 新增批量充值
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddRecharge")
	public ModelAndView intoAddRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/recharge/addRecharge");
		BigDecimal sumMoney = BigDecimal.ZERO;

		//查询已开户企业
		CompanyInf company = new CompanyInf();
		company.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
		List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(company);
		companyList = companyList.stream().filter(c -> c.getIsPlatform().equals(IsPlatformEnum.ISOPEN_FALSE.getCode())).collect(Collectors.toList());
		
		List<BillingType> bList = billingTypeInfService.getBillingTypeInfList(new BillingType());
		List<BillingType> billingTypeList = bList.stream().filter(t -> !SpecAccountTypeEnum.A01.getbId().equals(t.getBId())).collect(Collectors.toList());

		LinkedList<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.rechargeSession);
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);	
		Page<BatchOrderList> page = new Page<>(startNum, pageSize, false);
		
		if (orderList != null && orderList.size() >= 1) {
			for (BatchOrderList batchOrderList : orderList) {
				sumMoney = sumMoney.add(batchOrderList.getAmount());
			}
			page.setTotal(orderList.size());
			List<BatchOrderList> list = PagePersonUtil.getPersonInfPageList1(startNum, pageSize, orderList);
			list.forEach(o ->{
				page.add(o);
			});
		} else {
			page.setTotal(0);
		}
		PageInfo<BatchOrderList> pageList = new PageInfo<BatchOrderList>(page);
		mv.addObject("pageInfo", pageList);
		mv.addObject("count", pageList.getTotal());
		mv.addObject("sumMoney", sumMoney.doubleValue());
		mv.addObject("accountType", UserType.TYPE100);
		mv.addObject("companyList", companyList);
		mv.addObject("billingTypeList", billingTypeList);
		return mv;
	}

	/**
	 * 批量充值提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addRechargeCommit")
	@ResponseBody
	public Map<String, Object> addRechargeCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		LinkedList<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.rechargeSession);
		if (orderList == null || orderList.size() < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		
		try {
			int i = batchOrderService.addBatchOrderAndOrderList(req, orderList, TransCode.MB50.getCode(), UserType.TYPE100.getCode());
			if (i > 0) {
				jedisClusterUtils.del(OrderConstants.rechargeSession);
			}
		} catch (Exception e) {
			logger.error("## 新增批量充值订单出错----->>[{}]", e.getMessage());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增批量充值失败，请重新添加");
		}
		return resultMap;
	}

	/**
	 * 批量充值订单详情
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoViewRecharge")
	public ModelAndView intoViewRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/recharge/viewRecharge");
		
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		
		BatchOrder order = batchOrderService.getBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		order.setSumAmount(NumberUtils.RMBCentToYuan(order.getSumAmount()));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<BatchOrderList> pageList = batchOrderListService.getBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		return mv;
	}

	/**
	 * 进入批量充值订单编辑
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditRecharge")
	public ModelAndView intoEditRecharge(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/recharge/editRecharge");
		
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));

		BatchOrder order = batchOrderService.getBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<BatchOrderList> pageList = batchOrderListService.getBatchOrderListPage(startNum, pageSize, orderId);
		
		List<BatchOrderList> list = new ArrayList<>();
		list = batchOrderListService.getBatchOrderListByOrderId(orderId);
		
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("bizType", list.get(0).getBizType());
		mv.addObject("bizTypeName", SpecAccountTypeEnum.findByBId(list.get(0).getBizType()).getName());
		mv.addObject("accountType", list.get(0).getAccountType());
		mv.addObject("accountTypeName", UserType.findByCode(list.get(0).getAccountType()).getValue());
		return mv;
	}

	/**
	 * 删除批量充值订单
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteRechargeCommit")
	@ResponseBody
	public Map<String, Object> deleteRechargeCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		try {
			int i = batchOrderService.deleteOpenAccountOrRechargeCommit(orderId, user);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除批量充值订单失败");
			}
		} catch (Exception e) {
			logger.error("## 删除批量充值订单出错,orderId--->{}", orderId, e);
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "删除批量充值订单失败");
		}
		return resultMap;
	}

	/**
	 * 提交批量充值订单
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrderCommit")
	@ResponseBody
	public Map<String, Object> addOrderCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			BatchOrder order = batchOrderService.getBatchOrderById(orderId);
			if (BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat())) {
				int i = batchOrderService.updateBatchOrderAndOrderListByOrderStat(orderId, BatchOrderStat.BatchOrderStat_30.getCode(), user);
				if (i < 1) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "提交批量充值订单失败");
					return resultMap;
				}
			}
			int i = batchOrderService.batchTransferAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "提交批量开户订单失败");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 提交批量充值订单，订单号：[{}]，出错", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 批量充值添加充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrderListCommit")
	@ResponseBody
	public Map<String, Object> addOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		String bizType = StringUtil.nullToString(req.getParameter("bizType")); 
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		
		try {
			BatchOrderList orderList = new BatchOrderList();
			orderList.setOrderId(orderId);
			orderList.setUserName(StringUtil.nullToString(req.getParameter("name")));
			orderList.setPhoneNo(StringUtil.nullToString(req.getParameter("phone")));
			orderList.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			orderList.setAccountType(StringUtil.nullToString(req.getParameter("accountType")));
			orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			orderList.setOrderStat2(BatchOrderStat.BatchOrderStat_30.getCode());
			orderList.setOrderStat3(BatchOrderStat.BatchOrderStat_40.getCode());
			orderList.setOrderStat4(BatchOrderStat.BatchOrderStat_00.getCode());
			orderList.setAmount(new BigDecimal(NumberUtils.RMBYuanToCent(StringUtil.nullToString(req.getParameter("money")))));
			
			String[] bizTypes = {bizType};
			
			List<BatchOrderList> orderList2 = batchOrderListService.getBatchOrderListByOrder(orderList);
			if (orderList2 != null && orderList2.size() > 0) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "电话号码重复！！！");
				return resultMap;
			}
			int i = batchOrderListService.addOrderListByUpdateOpenAccount(orderList, user, bizTypes);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加充值信息失败，请稍后再试");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量充值添加充值信息出错", e);
		}
		return resultMap;
	}

	/**
	 * 删除批量充值的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteOrderListCommit")
	@ResponseBody
	public Map<String, Object> deleteOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			int i = batchOrderListService.deleteBatchOrderList(req);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除充值名单失败，请稍后再试");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 删除批量充值信息出错", e);
		}
		return resultMap;
	}

	/**
	 * 添加充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addAccountInf")
	@ResponseBody
	public Map<String, Object> addAccountInf(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			String phone = StringUtil.nullToString(req.getParameter("phone"));
			String money = StringUtil.nullToString(req.getParameter("money"));
			
			boolean flag = true;
			
			LinkedList<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.rechargeSession);
			if (orderList != null && orderList.size() >= 1) {
				orderList.forEach(o ->{
					if (o.getPhoneNo().equals(phone)) {
						resultMap.put("status", Boolean.FALSE);
						resultMap.put("msg", "电话号码重复！！！");
						return;
					}
				});
			} else {
				orderList = new LinkedList<>();
			}
			
			if (!flag) {
				return resultMap;
			}
			
			BatchOrderList order = new BatchOrderList();
			order.setPuId(IdUtil.getNextId().replace("-", ""));
			order.setUserName(StringUtil.nullToString(req.getParameter("name")));
			order.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			order.setPhoneNo(phone);
			order.setAmount(new BigDecimal(NumberUtils.RMBCentToYuan(NumberUtils.RMBYuanToCent(money))));
			orderList.addFirst(order);
			
			jedisClusterUtils.setex(OrderConstants.rechargeSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量充值新增，添加充值名单出错！", e);
		}
		return resultMap;
	}

	/**
	 * 删除批量充值用户信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteAccountInf")
	@ResponseBody
	public Map<String, Object> deleteAccountInf(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		try {
			String puId = req.getParameter("puId");
			List<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.rechargeSession);
			orderList = orderList.stream().filter(o -> !o.getPuId().equals(puId)).collect(Collectors.toList());
			jedisClusterUtils.setex(OrderConstants.rechargeSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量充值新增，删除充值用户名单出错", e);
		}
		return resultMap;
	}

}
