package com.cn.thinkx.ecom.platforder.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.platforder.service.OrderShipService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("platforder/orderShip")
public class OrderShipController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderShipService orderShipService;

	@RequestMapping(value="getOrderShipList")
	public ModelAndView getOrderShipList(HttpServletRequest req, OrderShip orderShip) {
		ModelAndView mv = new ModelAndView("platforder/listOrderShip");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<OrderShip> pageInfo = null;
		try {
			pageInfo = orderShipService.getOrderShipListPage(startNum, pageSize, orderShip);
		} catch (Exception e) {
			logger.error("## 查询订单收货地址失败,[{}]", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("orderShip", orderShip);
		return mv;
	}
}
