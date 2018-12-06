/*package com.cn.thinkx.oms.specialAccount.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrder;
import com.cn.thinkx.oms.specialAccount.model.SpeAccountBatchOrderList;
import com.cn.thinkx.oms.specialAccount.util.OrderConstants;
import com.cn.thinkx.oms.specialAccount.util.PagePersonUtil;
import com.cn.thinkx.oms.sys.model.User;
import com.cn.thinkx.pms.base.utils.BaseConstants;
import com.cn.thinkx.pms.base.utils.BaseConstants.OMSOrderStat;
import com.cn.thinkx.pms.base.utils.BaseConstants.OMSOrderType;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value="speaccount/batch")
public class SpeBatchOpenAccountController {

	@Autowired
	private SpeAccountBatchOrderService speAccountBatchOrderService;
	
	@Autowired
	private SpeAccountBatchOrderListService speAccountBatchOrderListService;
	
	*//**
	 * 批量开户查看
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/listOpenAccount")
	public ModelAndView listOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/listOpenAccount");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<SpeAccountBatchOrder> pageList = null;
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		order.setOrderType(OMSOrderType.orderType_100.getCode());
		try {
			
			pageList = speAccountBatchOrderService.getSpeAccountBatchOrderPage(startNum, pageSize, order, req);
		} catch (Exception e) {
			logger.error("## 批量开户查询列表信息出错", e);
		}
		mv.addObject("order", order);
		mv.addObject("mapOrderStat", OMSOrderStat.values());
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}
	
	
	*//**
	 * 进入开户页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoAddOpenAccount")
	public ModelAndView intoAddOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/addOpenAccount");
		LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		Page<SpeAccountBatchOrderList> page = new Page<>(startNum, pageSize, false);
		if (orderList != null) {
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
		mv.addObject("count", page.getTotal());
		return mv;
	}
	
	*//**
	 * 批量开户提交
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/addOpenAccountCommit")
	@ResponseBody
	public ModelMap AddOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.addAttribute("status", Boolean.TRUE);
		int i = 0;
		SpeAccountBatchOrder order = new SpeAccountBatchOrder();
		LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
		if (orderList == null) {
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "没有添加任何数据！！！");
			return resultMap;
		}
		User user = this.getCurrUser(req);
		order.setOrderName(StringUtil.nullToString(req.getParameter("orderName")));
		order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_10.getCode());
		order.setOrderType(BaseConstants.OMSOrderType.orderType_100.getCode());
		order.setCreateUser(user.getId().toString());
		order.setUpdateUser(user.getId().toString());
		try {
			i = speAccountBatchOrderService.addSpeAccountBatchOrder(order, orderList);
			if (i > 0) {
				JedisClusterUtils.getInstance().del(OrderConstants.speBathOpenAccountSession);
			}
		} catch (Exception e) {
			logger.error("新增出错---->>[{}]", e.getMessage());
			resultMap.addAttribute("status", Boolean.FALSE);
			resultMap.addAttribute("msg", "新增失败，请重新添加");
		}

		return resultMap;
	}

	*//**
	 * 订单详情
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoViewOpenAccount")
	public ModelAndView intoViewOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/viewOpenAccount");
		String orderId = req.getParameter("orderId");
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BaseConstants.OMSOrderStat.findStat(order.getOrderStat()));
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		return mv;
	}

	*//**
	 * 进入编辑页面
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/intoEditOpenAccount")
	public ModelAndView intoEditOpenAccount(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("specialAccount/batchOpenAccount/editOpenAccount");
		String orderId = req.getParameter("orderId");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderByOrderId(orderId);
		order.setOrderStat(BaseConstants.OMSOrderStat.findStat(order.getOrderStat()));
		int startNum = parseInt(req.getParameter("pageNum"), 1);
		int pageSize = parseInt(req.getParameter("pageSize"), 10);
		PageInfo<SpeAccountBatchOrderList> pageList = speAccountBatchOrderListService.getSpeAccountBatchOrderListPage(startNum, pageSize, orderId);
		mv.addObject("order", order);
		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		return mv;
	}

	*//**
	 * 删除批量开户的订单信息
	 * 
	 * @param req
	 * @param response
	 * @return
	 *//*
	@RequestMapping(value = "/deleteOpenAccountCommit")
	@ResponseBody
	public ModelMap deleteOpenAccountCommit(HttpServletRequest req, HttpServletResponse response) {
		ModelMap resultMap = new ModelMap();
		resultMap.put("status", Boolean.TRUE);
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			order.setOrderStat(BaseConstants.OMSOrderStat.orderStat_19.getCode());
			speAccountBatchOrderService.updateSpeAccountBatchOrder(order);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 删除订单号：[{}]批量开户的订单，出错", orderId, e);
		}
		return resultMap;
	}

	*//**
	 * 提交批量开户的订单信息
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
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		User user = this.getCurrUser(req);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BaseConstants.OMSOrderStat.orderStat_10.getCode().equals(order.getOrderStat())) {
				speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(orderId, user, BaseConstants.OMSOrderStat.orderStat_10.getCode());
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error("## 提交批量开户的订单信息，订单号：[{}],出错", orderId, e);
		}
		return resultMap;
	}

	*//**
	 * 重新提交批量开户的订单信息
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
		String orderId = StringUtil.nullToString(req.getParameter("orderId"));
		User user = this.getCurrUser(req);
		try {
			SpeAccountBatchOrder order = speAccountBatchOrderService.getSpeAccountBatchOrderById(orderId);
			if (BaseConstants.OMSOrderStat.orderStat_90.getCode().equals(order.getOrderStat())) {
				speAccountBatchOrderService.batchSpeAccountBatchOpenAccountITF(orderId, user, BaseConstants.OMSOrderStat.orderStat_90.getCode());
			}
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			logger.error(" ## 重新提交批量开户的订单信息，订单号：[{}],出错", orderId, e);
		}
		return resultMap;
	}

	*//**
	 * 批量开户编辑页面，添加开户名单
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
		User user = this.getCurrUser(req);
		try {
			SpeAccountBatchOrderList orderList = new SpeAccountBatchOrderList();
			orderList.setOrderId(StringUtil.nullToString(req.getParameter("orderId")));
			orderList.setUserName(StringUtil.nullToString(req.getParameter("name")));
			orderList.setPhoneNo(StringUtil.nullToString(req.getParameter("phone")));
			orderList.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			orderList.setCompanydCode(StringUtil.nullToString(req.getParameter("companydCode")));
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
			logger.error("## 批量开户编辑页面，添加开户名单,订单号：[{}],出错", req.getParameter("orderId"), e);
		}
		return resultMap;
	}

	*//**
	 * 批量开户编辑页面，删除开户名单
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
			logger.error("## 批量开户编辑页面，删除开户名单出错", e);
		}
		return resultMap;
	}

	*//**
	 * 批量开户新增，添加开户名单
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
		String phone = StringUtil.nullToString(req.getParameter("phone"));
		String companydCode = StringUtil.nullToString(req.getParameter("companydCode"));
		try {
			SpeAccountBatchOrderList person = new SpeAccountBatchOrderList();
			person.setPuid(UUID.randomUUID().toString().replace("-", ""));
			person.setUserName(StringUtil.nullToString(req.getParameter("name")));
			person.setUserCardNo(StringUtil.nullToString(req.getParameter("card")));
			person.setPhoneNo(phone);
			person.setCompanydCode(companydCode);
			LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
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
			orderList.addFirst(person);
			JedisClusterUtils.getInstance().setex(OrderConstants.speBathOpenAccountSession, JSON.toJSON(orderList).toString(), 1800); // 设置有效时间30分钟
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，添加开户名单出错！", e);
		}
		return resultMap;
	}

	*//**
	 * 批量开户新增，删除开户名单
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
			LinkedList<SpeAccountBatchOrderList> orderList = PagePersonUtil.getRedisBatchOrderList(OrderConstants.speBathOpenAccountSession);
			orderList.forEach(o ->{
				if (o.getPuid().equals(puid)) {
					orderList.remove(o);
					return;
				}
			});
//			for (SpeAccountBatchOrderList personInf : orderList) {
//				if (personInf.getPuid().equals(puid)) {
//					orderList.remove(personInf);
//					break;
//				}
//			}
			JedisClusterUtils.getInstance().setex(OrderConstants.speBathOpenAccountSession, JSON.toJSON(orderList).toString(), 1800);
		} catch (Exception e) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "系统故障，请稍后再试");
			logger.error("## 批量开户新增，删除开户名单出错", e);
		}

		return resultMap;
	}
}
*/