package com.cn.thinkx.ecom.order.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.ExcelUtil;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.cn.thinkx.ecom.eshop.service.EshopInfService;
import com.cn.thinkx.ecom.order.domain.OrderDetailUpload;
import com.cn.thinkx.ecom.order.domain.OrderInf;
import com.cn.thinkx.ecom.order.service.OrderInfService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping(value = "order")
public class OrderInfController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderInfService orderInfService;
	
	@Autowired
	private EshopInfService eshopInfService;

	/**
	 * 电商交易流水明细列表
	 * 
	 * @param req
	 * @param orderInf
	 * @return
	 */
	@GetMapping(value = "/listOrderDetail")
	public ModelAndView listOrder(HttpServletRequest req, OrderInf orderInf) {
		ModelAndView mv = new ModelAndView("order/listOrderDetail");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<OrderInf> pageList = orderInfService.getOrderDetailPage(startNum, pageSize, orderInf);
			List<EshopInf> eshopNameList = eshopInfService.getList();
			mv.addObject("orderInf", orderInf);
			mv.addObject("pageInfo", pageList);
			mv.addObject("eshopNameList", eshopNameList);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("orderTypeList", Constants.OrderType.values());
		} catch (Exception e) {
			logger.error("## 查询电商交易流水列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 电商交易流水明细列表
	 * 
	 * @param req
	 * @param resp
	 * @param orderInf
	 * @return
	 */
	@PostMapping(value = "/listOrderDetail")
	public ModelAndView listOrderDetail(HttpServletRequest req, HttpServletResponse resp, OrderInf orderInf) {
		ModelAndView mv = new ModelAndView("order/listOrderDetail");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if (orderInf.getQueryType() == null) {
				orderInf.setBeginTime(req.getParameter("beginTime"));
				orderInf.setEndTime(req.getParameter("endTime"));
			} else if (orderInf.getQueryType() == "cur" || orderInf.getQueryType().equals("cur")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				orderInf.setBeginTime(sdf1.format(DateUtil.toDateStart(sdf.format(new Date()))));
				orderInf.setEndTime(sdf1.format(DateUtil.toDateEnd(sdf.format(new Date()))));
			} else if (orderInf.getQueryType() == "his" || orderInf.getQueryType().equals("his")) {
				orderInf.setBeginTime(req.getParameter("beginTime"));
				orderInf.setEndTime(req.getParameter("endTime"));
			}
			PageInfo<OrderInf> pageList = orderInfService.getOrderDetailPage(startNum, pageSize, orderInf);
			List<EshopInf> eshopNameList = eshopInfService.getList();
			mv.addObject("orderInf", orderInf);
			mv.addObject("pageInfo", pageList);
			mv.addObject("eshopNameList", eshopNameList);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("orderTypeList", Constants.OrderType.values());
		} catch (Exception e) {
			logger.error("## 查询电商交易流水列表信息出错", e);
		}
		return mv;
	}
	
	/**
	 * 电商交易流水汇总列表
	 * 
	 * @param req
	 * @param orderInf
	 * @return
	 */
	@GetMapping(value = "/listOrderSummarizing")
	public ModelAndView listOrderSummarizing(HttpServletRequest req, OrderInf orderInf) {
		ModelAndView mv = new ModelAndView("order/listOrderSummarizing");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			PageInfo<OrderInf> pageList = orderInfService.getOrderSummarizingPage(startNum, pageSize, orderInf);
			
			List<EshopInf> eshopNameList = eshopInfService.getList();
			mv.addObject("orderInf", orderInf);
			mv.addObject("pageInfo", pageList);
			mv.addObject("eshopNameList", eshopNameList);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("orderTypeList", Constants.OrderType.values());
		} catch (Exception e) {
			logger.error("## 查询电商交易流水列表信息出错", e);
		}
		return mv;
	}

	/**
	 * 电商交易流水汇总列表
	 * 
	 * @param req
	 * @param resp
	 * @param orderInf
	 * @return
	 */
	@PostMapping(value = "/listOrderSummarizing")
	public ModelAndView listOrderSummarizing(HttpServletRequest req, HttpServletResponse resp, OrderInf orderInf) {
		ModelAndView mv = new ModelAndView("order/listOrderSummarizing");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		try {
			if (orderInf.getQueryType() == null) {
				orderInf.setBeginTime(req.getParameter("beginTime"));
				orderInf.setEndTime(req.getParameter("endTime"));
			} else if (orderInf.getQueryType() == "cur" || orderInf.getQueryType().equals("cur")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				orderInf.setBeginTime(sdf1.format(DateUtil.toDateStart(sdf.format(new Date()))));
				orderInf.setEndTime(sdf1.format(DateUtil.toDateEnd(sdf.format(new Date()))));
			} else if (orderInf.getQueryType() == "his" || orderInf.getQueryType().equals("his")) {
				orderInf.setBeginTime(req.getParameter("beginTime"));
				orderInf.setEndTime(req.getParameter("endTime"));
			}
			PageInfo<OrderInf> pageList = orderInfService.getOrderSummarizingPage(startNum, pageSize, orderInf);
			List<EshopInf> eshopNameList = eshopInfService.getList();
			mv.addObject("orderInf", orderInf);
			mv.addObject("pageInfo", pageList);
			mv.addObject("eshopNameList", eshopNameList);
			mv.addObject("channelList", Constants.ChannelEcomType.values());
			mv.addObject("orderTypeList", Constants.OrderType.values());
		} catch (Exception e) {
			logger.error("## 查询电商交易流水汇总信息出错", e);
		}
		return mv;
	}
	
	/**
	 * 交易流水明细列表导出表格
	 * 
	 * @param req
	 * @param resp
	 * @param orderInf
	 */
	@PostMapping(value = "/uploadOrderDetail")
	public void uploadOrderDetail(HttpServletRequest req, HttpServletResponse resp, OrderInf orderInf){
		try{
			if (orderInf.getQueryType() == "cur" || orderInf.getQueryType().equals("cur")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				orderInf.setBeginTime(sdf1.format(DateUtil.toDateStart(sdf.format(new Date()))));
				orderInf.setEndTime(sdf1.format(DateUtil.toDateEnd(sdf.format(new Date()))));
			}
			List<OrderInf> orderList = orderInfService.listOrderDetail(orderInf);
			List<OrderDetailUpload> orderUploadList = new ArrayList<OrderDetailUpload>();
			for (int i = 0; i < orderList.size(); i++) {
				OrderDetailUpload odu = new OrderDetailUpload();
				EshopInf eshop = new EshopInf();
				eshop.setMchntCode(orderList.get(i).getMerchantNo());
				eshop.setShopCode(orderList.get(i).getShopNo());
				EshopInf eshopInf = eshopInfService.selectByEshopInf(eshop);
				if (eshopInf != null) 
					odu.setEshopName(eshopInf.getEshopName());
				else
					odu.setEshopName("查无此商城");
				if (!StringUtil.isNullOrEmpty(orderList.get(i).getPersonalName())) 
					odu.setPersonalName(orderList.get(i).getPersonalName());
				else
					odu.setPersonalName("查无此用户");
				try {
					odu.setSettleDate(DateUtil.getHalfFormatStr(orderList.get(i).getCreateTime()));
				} catch (Exception e) {
					logger.error("## 时间转换出错", e);
				}
				odu.setChannelName(Constants.ChannelEcomType.findByCode(orderList.get(i).getChannel()).getValue());
				odu.setOrderNo(orderList.get(i).getId());
				odu.setRouterOrderNo(orderList.get(i).getRouterOrderNo());
				odu.setTransTime(orderList.get(i).getCreateTime());
				odu.setTransType(Constants.OrderType.findByCode(orderList.get(i).getOrderType()).getValue());
				odu.setTxnAmount(orderList.get(i).getTxnAmount());
				orderUploadList.add(odu);
			}
			String titlerow = "电商交易明细报表";
			String[] titlehead = new String[]{"电商流水号","外部流水号","清算日期","用户名称","商城名称","电商名称","交易金额（元）","交易时间","交易状态",};
			ExcelUtil<OrderDetailUpload> ex = new ExcelUtil<OrderDetailUpload>();
			ex.upLoadExcel(titlerow, titlerow, DateUtil.getFormatStringFormString(orderInf.getBeginTime(), DateUtil.FORMAT_YYYY_MM_DD), DateUtil.getFormatStringFormString(orderInf.getEndTime(), DateUtil.FORMAT_YYYY_MM_DD), titlehead, null, null, orderUploadList, OrderDetailUpload.class, null, resp);
		} catch (Exception e) {
			logger.error("## 电商交易流水导出报表信息出错", e);
		}
	}
	
}
