package com.ebeijia.zl.web.oms.batchOrder.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.common.utils.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.ebeijia.zl.web.oms.utils.OrderConstants;
import com.ebeijia.zl.web.oms.utils.PagePersonUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value="batch/openAccount")
public class BatchOpenAccountController {
	
	Logger logger = LoggerFactory.getLogger(BatchOpenAccountController.class);

	@Autowired
	private BatchOrderService batchOrderService;
	
	@Autowired
	private BatchOrderListService batchOrderListService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
	@Autowired
	private BillingTypeService billingTypeInfService;

	@Autowired
	private CompanyInfFacade companyInfFacade;
	
	/**
	 * 批量开户查看
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listOpenAccount")
	public ModelAndView listOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/openAccount/listOpenAccount");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		
		BatchOrder order = new BatchOrder();
		order.setOrderType(TransCode.CW80.getCode());
		
		PageInfo<BatchOrder> pageList = null;
		try {
			pageList = batchOrderService.getBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 批量开户查询列表信息出错", e);
		}

		//查询已开户企业
		CompanyInf company = new CompanyInf();
		company.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
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
		mv.addObject("accountType", UserType.TYPE100.getCode());
		mv.addObject("companyList", companyList);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	/**
	 * 进入开户页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoAddOpenAccount")
	public ModelAndView intoAddOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/openAccount/addOpenAccount");
		//查询已开户企业
		CompanyInf company = new CompanyInf();
		company.setIsOpen(IsOpenEnum.ISOPEN_TRUE.getCode());
		List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(company);
		companyList = companyList.stream().filter(c -> c.getIsPlatform().equals(IsPlatformEnum.ISOPEN_FALSE.getCode())).collect(Collectors.toList());
		//查询所有账户类型
		List<BillingType> billingTypeList = billingTypeInfService.getBillingTypeInfList(new BillingType());
		//从缓存中查询是否存在数据
		LinkedList<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.openAccountSession);
		//批量订单分页
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		Page<BatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null && orderList.size() >= 1) {
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
		mv.addObject("count", page.getTotal());
		mv.addObject("accountType", UserType.TYPE100.getCode());
		mv.addObject("companyList", companyList);
		mv.addObject("billingTypeList", billingTypeList);
		return mv;
	}
	
	/**
	 * 批量开户数据提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOpenAccountCommit")
	@ResponseBody
	public Map<String, Object> addOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		LinkedList<BatchOrderList> orderLists = batchOrderListService.getRedisBatchOrderList(OrderConstants.openAccountSession);
		if (orderLists == null || orderLists.size() < 1) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		try {
			int i = batchOrderService.addBatchOrderAndOrderList(req, orderLists, TransCode.CW80.getCode(), UserType.TYPE100.getCode());
			if (i > 0) {
				jedisClusterUtils.del(OrderConstants.openAccountSession);
			}
		} catch (Exception e) {
			logger.error("新增批量开户信息出错---->>[{}]", e.getMessage());
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "新增开户信息失败，请重新添加");
		}
		return resultMap;
	}

	/**
	 * 订单详情
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoViewOpenAccount")
	public ModelAndView intoViewOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/openAccount/viewOpenAccount");
		String orderId = req.getParameter("orderId");
		BatchOrder order = batchOrderService.getBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<BatchOrderList> pageList = batchOrderListService.getBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		return mv;
	}

	/**
	 * 进入编辑页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/intoEditOpenAccount")
	public ModelAndView intoEditOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("batch/openAccount/editOpenAccount");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		BatchOrder order = batchOrderService.getBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<BatchOrderList> pageList = batchOrderListService.getBatchOrderListPage(startNum, pageSize, orderId);
		
		List<BatchOrderList> list = new ArrayList<>();
		list = batchOrderListService.getBatchOrderListByOrderId(orderId);
		list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BatchOrderList::getBizType))), ArrayList::new));
		List<SpecAccountTypeEnum> billingTypeList = new ArrayList<>();
		for (BatchOrderList o : list) {
			billingTypeList.add(SpecAccountTypeEnum.findByBId(o.getBizType()));
		}
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("billingTypeList", billingTypeList);
		mv.addObject("accountType", UserType.TYPE100.getCode());
		mv.addObject("accountTypeName", UserType.TYPE100.getValue());
		return mv;
	}

	/**
	 * 删除批量开户的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteOpenAccountCommit")
	@ResponseBody
	public Map<String, Object> deleteOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", Boolean.TRUE);
		
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			int i = batchOrderService.deleteOpenAccountOrRechargeCommit(orderId, user);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除批量开户订单失败");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 删除批量开户订单出错，orderId--->{}", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 提交批量开户的订单信息，进行开户
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
					resultMap.put("msg", "提交批量开户订单失败");
					return resultMap;
				}
			}
			int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "提交批量开户订单失败");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 提交批量开户订单信息出错,orderId--->{}", orderId, e);
		}
		return resultMap;
	}
	
	/**
	 * 批量开户编辑页面，添加开户用户名单
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
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			//根据orderId查询订单明细信息
			List<BatchOrderList> list = batchOrderListService.getBatchOrderListByOrderId(orderId);
			list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BatchOrderList::getBizType))), ArrayList::new));
			String[] bizType = {};
			for (BatchOrderList o : list) {
				String[] type = {o.getBizType()};
				bizType = type.clone();
			}
			
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
			List<BatchOrderList> orderLists = batchOrderListService.getBatchOrderListByOrder(orderList);
			if (orderLists != null && orderLists.size() > 0) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "电话号码重复！！！");
				return resultMap;
			}
			int i = batchOrderListService.addOrderListByUpdateOpenAccount(orderList, user, bizType);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加开户名单失败，请稍后再试");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户编辑页面，添加开户名单出错,orderId--->{}", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 批量开户编辑页面，删除开户名单
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
				resultMap.put("msg", "删除开户名单失败，请稍后再试");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户编辑页面，删除开户名单出错", e);
		}
		return resultMap;
	}

	/**
	 * 批量开户新增，添加开户名单
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
		
		String phone = StringUtil.nullToString(req.getParameter("phone"));
		boolean flag = true;
		
		try {
			LinkedList<BatchOrderList> orderList = batchOrderListService.getRedisBatchOrderList(OrderConstants.openAccountSession);
			if (orderList != null && orderList.size() >= 1) {
				for (BatchOrderList o : orderList) {
					if (o.getPhoneNo().equals(phone)) {
						resultMap.put("status", Boolean.FALSE);
						resultMap.put("msg", "电话号码重复！！！");
						flag = false;
						break;
					}
				}
			} else {
				orderList = new LinkedList<>();
			}
			
			if (!flag) {
				return resultMap;
			}
			
			BatchOrderList personOrder = new BatchOrderList();
			personOrder.setPuId(IdUtil.getNextId().replace("-", ""));
			personOrder.setUserName(StringUtil.nullToString(req.getParameter("name")));
			personOrder.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			personOrder.setPhoneNo(phone);
			orderList.addFirst(personOrder);
			
			jedisClusterUtils.setex(OrderConstants.openAccountSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，添加开户名单出错！", e);
		}
		return resultMap;
	}

	/**
	 * 批量开户新增，删除开户名单
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
			List<BatchOrderList> batchOrderLists = batchOrderListService.getRedisBatchOrderList(OrderConstants.openAccountSession);
			batchOrderLists = batchOrderLists.stream().filter(o -> !o.getPuId().equals(puId)).collect(Collectors.toList());
			jedisClusterUtils.setex(OrderConstants.openAccountSession, JSON.toJSON(batchOrderLists).toString(), 1800);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，删除开户用户名单出错", e);
		}
		return resultMap;
	}
}
