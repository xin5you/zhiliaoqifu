package com.cn.thinkx.oms.specialAccount.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderStat;
import com.cn.thinkx.oms.common.util.OmsEnum.BatchOrderType;
import com.cn.thinkx.oms.specialAccount.model.BillingTypeInf;
import com.cn.thinkx.oms.specialAccount.model.CompanyInf;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.service.BillingTypeInfService;
import com.cn.thinkx.oms.specialAccount.service.CompanyInfService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderListService;
import com.cn.thinkx.oms.specialAccount.service.SpeAccountBatchOrderService;
import com.cn.thinkx.oms.specialAccount.util.OrderConstants;
import com.cn.thinkx.oms.specialAccount.util.PagePersonUtil;
import com.cn.thinkx.oms.sys.model.User;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
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
	@Qualifier("billingTypeInfService")
	private BillingTypeInfService billingTypeInfService;

	@Autowired
	@Qualifier("companyInfService")
	private CompanyInfService companyInfService;
	
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
		order.setOrderType(BatchOrderType.BatchOrderType_100.getCode());
		try {
			pageList = speAccountBatchOrderService.getSpeAccountBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 批量开户查询列表信息出错", e);
		}
		mv.addObject("order", order);
		mv.addObject("mapOrderStat", BatchOrderStat.values());
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
		List<CompanyInf> companyList = companyInfService.getCompanyInfList(new CompanyInf());
		List<BillingTypeInf> billingTypeList = billingTypeInfService.getBillingTypeInfList(new BillingTypeInf());
		
		/*SpeAccountBatchOrderList order = new SpeAccountBatchOrderList();
		order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
//		order.setOrderStat2(BatchOrderStat.BatchOrderStat_30.getCode());
		LinkedList<SpeAccountBatchOrderList> orderList = (LinkedList<SpeAccountBatchOrderList>) speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(order);
		
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		Page<SpeAccountBatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null) {
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
		mv.addObject("count", page.getTotal());*/
		mv.addObject("accountTypeList", UserType.values());
		mv.addObject("companyList", companyList);
		mv.addObject("billingTypeList", billingTypeList);
		
		
		LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		Page<SpeAccountBatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null) {
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
		int i = 0;
		LinkedList<SpeAccountBatchOrderList> orderLists = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		/*SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
		orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		LinkedList<SpeAccountBatchOrderList> orderLists = (LinkedList<SpeAccountBatchOrderList>) speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(orderList);*/
		
		if (orderLists == null) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));
		order.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
		order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
		order.setOrderType(BatchOrderType.BatchOrderType_100.getCode());
		order.setCreateUser(user.getId().toString());
		order.setCreateTime(System.currentTimeMillis());
		order.setUpdateUser(user.getId().toString());
		order.setUpdateTime(System.currentTimeMillis());
		
		order.setAccountType(StringUtil.nullToString(req.getParameter("accountType")));
		order.setBizType(StringUtil.nullToString(req.getParameter("bizType")));
		try {
			i = speAccountBatchOrderService.addSpeAccountBatchOrder(order, orderLists);
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
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
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
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			order.setOrderStat(BatchOrderStat.BatchOrderStat_20.getCode());
			order.setUpdateTime(System.currentTimeMillis());
			order.setUpdateUser(user.getId());
			speAccountBatchOrderService.updateSpeAccountBatchOrder(order);
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
				speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_10.getCode());
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
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			orderList.setOrderId(StringUtil.nullToString(req.getParameter("orderId")));
			orderList.setUserName(StringUtil.nullToString(req.getParameter("name")));
			orderList.setPhoneNo(StringUtil.nullToString(req.getParameter("phone")));
			orderList.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			orderList.setCompanyId(StringUtil.nullToString(req.getParameter("companyId")));
			/*orderList.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			orderList.setOrderListId(UUID.randomUUID().toString());
			orderList.setCreateUser(user.getId().toString());
			orderList.setUpdateUser(user.getId().toString());
			orderList.setCreateTime(System.currentTimeMillis());
			orderList.setUpdateTime(System.currentTimeMillis());*/
			List<SpeAccountBatchOrderList> orderList2 = speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(orderList);
			if (orderList2 != null) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "电话号码重复！！！");
				return resultMap;
			}
			speAccountBatchOrderListService.addOrderList(orderList, user);
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
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);
		String phone = StringUtil.nullToString(req.getParameter("phone"));
		try {
			SpeAccountBatchOrderList personOrder = new SpeAccountBatchOrderList();
			personOrder.setPuid(UUID.randomUUID().toString());
			personOrder.setUserName(StringUtil.nullToString(req.getParameter("name")));
			personOrder.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			personOrder.setPhoneNo(phone);
			LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
			if (orderList == null) {
				orderList = new LinkedList<SpeAccountBatchOrderList>();
			}
			/*SpeAccountBatchOrderList order = new SpeAccountBatchOrderList();
			order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			LinkedList<SpeAccountBatchOrderList> orderList = (LinkedList<SpeAccountBatchOrderList>) speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(order);*/
			orderList.forEach(o ->{
				if (o.getPhoneNo().equals(phone)) {
					resultMap.put("status", Boolean.FALSE);
					resultMap.put("msg", "电话号码重复！！！");
					return;
				}
			});
			orderList.addFirst(personOrder);
			/*int i = speAccountBatchOrderListService.addOrderList(personOrder, user);
			if (i < 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("msg", "添加开户名单出错");
				return resultMap;
			}*/
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
			/*SpeAccountBatchOrderList order = new SpeAccountBatchOrderList();
			order.setOrderStat(BatchOrderStat.BatchOrderStat_10.getCode());
			LinkedList<SpeAccountBatchOrderList> batchOrderList = (LinkedList<SpeAccountBatchOrderList>) speAccountBatchOrderListService.getSpeAccountBatchOrderListByOrder(order);*/
			LinkedList<SpeAccountBatchOrderList> batchOrderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
			batchOrderList.forEach(o ->{
				if (o.getPuid().equals(puId)) {
					batchOrderList.remove(o);
					/*SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
					orderList.setOrderListId(orderListId);
					orderList.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
					HttpSession session = req.getSession();
					User user = (User)session.getAttribute(Constants.SESSION_USER);
					orderList.setUpdateUser(user.getId());
					orderList.setUpdateTime(System.currentTimeMillis());
					if (speAccountBatchOrderListService.updateSpeAccountBatchOrderList(orderList) < 1){
						logger.error("## 批量开户新增，删除开户名单{}失败", orderListId);
					}*/
					return;
				}
			});
			jedisClusterUtils.setex(OrderConstants.speBathOpenAccountSession, JSON.toJSON(batchOrderList).toString(), 1800);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，删除开户名单出错", e);
		}

		return resultMap;
	}
}
