package com.ebeijia.zl.web.oms.specialAccount.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.ebeijia.zl.basics.billingtype.domain.BillingTypeInf;
import com.ebeijia.zl.basics.billingtype.service.BillingTypeInfService;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrder;
import com.ebeijia.zl.web.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.ebeijia.zl.web.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.ebeijia.zl.web.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.ebeijia.zl.web.oms.specialAccount.util.OrderConstants;
import com.ebeijia.zl.web.oms.specialAccount.util.PagePersonUtil;
import com.ebeijia.zl.web.oms.sys.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value="speaccount/batch")
public class SpeBatchOpenAccountController {
	
	Logger logger = LoggerFactory.getLogger(SpeBatchOpenAccountController.class);

	@Autowired
	private SpeAccountBatchOrderService speAccountBatchOrderService;
	
	@Autowired
	private SpeAccountBatchOrderListService speAccountBatchOrderListService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;
	
	@Autowired
	private BillingTypeInfService billingTypeInfService;

	@Reference(check=false)
	private CompanyInfFacade companyInfFacade;
	
	/**
	 * 批量开户查看
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listOpenAccount")
	public ModelAndView listOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/listOpenAccount");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<SpeAccountBatchOrder> pageList = null;
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderType(TransCode.CW80.getCode());
		try {
			pageList = speAccountBatchOrderService.getSpeAccountBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 批量开户查询列表信息出错", e);
		}
		List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(new CompanyInf());
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
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/addOpenAccount");
		List<CompanyInf> companyList = companyInfFacade.getCompanyInfList(new CompanyInf());
		List<BillingTypeInf> billingTypeList = billingTypeInfService.getBillingTypeInfList(new BillingTypeInf());
		
		LinkedList<SpeAccountBatchOrderList> orderList = speAccountBatchOrderListService.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		Page<SpeAccountBatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null && orderList.size() >= 1) {
			page.setTotal(orderList.size());
			List<SpeAccountBatchOrderList> list = PagePersonUtil.getPersonInfPageList(startNum, pageSize, orderList);
			list.forEach(o ->{
				page.add(o);
			});
		} else {
			page.setTotal(0);
		}
		PageInfo<SpeAccountBatchOrderList> pageList = new PageInfo<SpeAccountBatchOrderList>(page);
		mv.addObject("pageInfo", pageList);
		mv.addObject("count", page.getTotal());
		mv.addObject("accountType", UserType.TYPE100.getCode());
		mv.addObject("companyList", companyList);
		mv.addObject("billingTypeList", billingTypeList);
		return mv;
	}
	
	/**
	 * 批量开户提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOpenAccountCommit")
	@ResponseBody
	public ModelMap AddOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		LinkedList<SpeAccountBatchOrderList> orderLists = speAccountBatchOrderListService.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		if (orderLists == null || orderLists.size() < 1) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		try {
			int i = speAccountBatchOrderService.addSpeAccountBatchOrder(req, orderLists, TransCode.CW80.getCode());
			if (i > 0) {
				jedisClusterUtils.del(OrderConstants.speBathOpenAccountSession);
			}
		} catch (Exception e) {
			logger.error("新增批量开户信息出错---->>[{}]", e.getMessage());
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "新增开户信息失败，请重新添加");
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
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/viewOpenAccount");
		String orderId = req.getParameter("orderId");
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
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
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/editOpenAccount");
		String orderId = req.getParameter("orderId");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BatchOrderStat.findStat(order.getOrderStat()));
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
		List<SpeAccountBatchOrderList> list = new ArrayList<>();
		list = speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrderId(orderId);
		list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SpeAccountBatchOrderList::getBizType))), ArrayList::new));
		List<SpecAccountTypeEnum> billingTypeList = new ArrayList<>();
		for (SpeAccountBatchOrderList o : list) {
			billingTypeList.add(SpecAccountTypeEnum.findByBId(o.getBizType()));
		}
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("billingTypeList", billingTypeList);
		mv.addObject("accountType", list.get(0).getAccountType());
		mv.addObject("accountTypeName", UserType.findByCode(list.get(0).getAccountType()).getValue());
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
	public ModelMap deleteOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			int i = speAccountBatchOrderService.deleteOpenAccountCommit(orderId, user);
			if (i == 0) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "删除批量开户订单失败");
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 删除批量开户订单号{}出错", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 提交批量开户的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrderCommit")
	@ResponseBody
	public ModelMap addOrderCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BatchOrderStat.BatchOrderStat_10.getCode().equals(order.getOrderStat())) {
				int i = speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_10.getCode());
				if (i == 0) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "提交批量开户订单失败");
					return resultMap;
				}
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 提交批量开户订单[{}]信息出错", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 重新提交批量开户的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrderAgainCommit")
	@ResponseBody
	public ModelMap addOrderAgainCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BatchOrderStat.BatchOrderStat_99.getCode().equals(order.getOrderStat())) {
				speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_99.getCode());
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error(" ## 重新提交批量开户订单[{}]信息出错", orderId, e);
		}
		return resultMap;
	}

	/**
	 * 批量开户编辑页面，添加开户名单
	 * 
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addOrderListCommit")
	@ResponseBody
	public ModelMap addOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		try {
			String orderId = StringUtil.nullToString(req.getParameter("orderId"));
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			orderList.setOrderId(orderId);
			orderList.setUserName(StringUtil.nullToString(req.getParameter("name")));
			orderList.setPhoneNo(StringUtil.nullToString(req.getParameter("phone")));
			orderList.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			orderList.setAccountType(StringUtil.nullToString(req.getParameter("accountType")));
			List<SpeAccountBatchOrderList> list = speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrderId(orderId);
			list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SpeAccountBatchOrderList::getBizType))), ArrayList::new));
			String[] bizType = {};
			for (SpeAccountBatchOrderList o : list) {
				String[] type = {o.getBizType()};
				bizType = type.clone();
			}
			List<SpeAccountBatchOrderList> orderList2 = speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(orderList);
			if (orderList2 != null && orderList2.size() > 0) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "电话号码重复！！！");
				return resultMap;
			}
			speAccountBatchOrderListService.addOrderList(orderList, user, bizType);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户编辑页面，添加开户名单出错,订单号[{}]", req.getParameter("orderId"), e);
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
	public ModelMap deleteOrderListCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		try {
			speAccountBatchOrderListService.deleteSpeAccountBatchOrderList(req);
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
	public ModelMap addAccountInf(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		
		String phone = StringUtil.nullToString(req.getParameter("phone"));
		try {
			SpeAccountBatchOrderList personOrder = new SpeAccountBatchOrderList();
			personOrder.setPuId(UUID.randomUUID().toString().replace("-", ""));
			personOrder.setUserName(StringUtil.nullToString(req.getParameter("name")));
			personOrder.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			personOrder.setPhoneNo(phone);
			
			LinkedList<SpeAccountBatchOrderList> orderList = speAccountBatchOrderListService.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
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
			orderList.addFirst(personOrder);
			jedisClusterUtils.setex(OrderConstants.speBathOpenAccountSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
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
	public ModelMap deleteAccountInf(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		try {
			String puId = req.getParameter("puId");
			LinkedList<SpeAccountBatchOrderList> batchOrderList = speAccountBatchOrderListService.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
			
			for (SpeAccountBatchOrderList o : batchOrderList) {
				if (o.getPuId().equals(puId)) {
					batchOrderList.remove(o);
				}
			}
			jedisClusterUtils.setex(OrderConstants.speBathOpenAccountSession, JSON.toJSON(batchOrderList).toString(), 1800);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，删除开户用户名单出错", e);
		}
		return resultMap;
	}
	
}
