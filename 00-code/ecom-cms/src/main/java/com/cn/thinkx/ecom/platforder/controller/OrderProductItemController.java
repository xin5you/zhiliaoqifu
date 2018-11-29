package com.cn.thinkx.ecom.platforder.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.platforder.service.OrderProductItemService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("platforder/orderProductItem")
public class OrderProductItemController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrderProductItemService orderProductItemService;

	/**
	 * 查询订单货品明细
	 * 
	 * @param req
	 * @param platfOrder
	 * @return
	 */
	@RequestMapping("getOrderProductItemList")
	public ModelAndView getOrderProductItemList(HttpServletRequest req,PlatfOrder platfOrder){
		ModelAndView mv = new ModelAndView("platforder/listOrderProductItem");
		int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
		int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
		PageInfo<PlatfOrder> pageInfo = null;
		try {
			pageInfo = orderProductItemService.getOrderShipListPage(startNum, pageSize, platfOrder);
		} catch (Exception e) {
			logger.error("## 查询订单货品明细失败,[{}]", e);
		}
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("platfOrder", platfOrder);
		mv.addObject("ecomCodeTypeList", Constants.GoodsEcomCodeType.values());
		return mv;
	}
}
