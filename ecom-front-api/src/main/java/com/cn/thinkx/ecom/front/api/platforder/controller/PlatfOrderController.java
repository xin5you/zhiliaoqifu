package com.cn.thinkx.ecom.front.api.platforder.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.OrderShip;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.platforder.service.PlatfOrderInfService;

/**
 * 订单信息
 * 
 * @author kpplg
 *
 */
@RestController
@RequestMapping(value = "goods/platfOrder")
public class PlatfOrderController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderInfService platfOrderService;
	
	@Autowired
	private OrderShipService orderShipService;

	/**
	 * 查询订单：通过会员id查询订单信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getPlatfOrderGoodsByMemberId")
	public ModelAndView getPlatfOrderGoodsByMemberId(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("ecom/platforder/listPlatforder");
		String payStatus = req.getParameter("payStatus");
		try {
			PlatfOrder po = new PlatfOrder();
			String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
			if (StringUtil.isNullOrEmpty(memberId)) {
				return null;
			}
			po.setMemberId(memberId);
			if (!StringUtil.isNullOrEmpty(payStatus)) {
				po.setPayStatus(payStatus); // 订单状态
			}
			List<PlatfOrder> platfOrderList = platfOrderService.getPlatfOrderGoodsByMemberId(po);
			mv.addObject("platfOrderList", platfOrderList);
			mv.addObject("payStatus", payStatus);
			// logger.info("##
			// ---->platfOrderList:[{}]",JSON.toJSONString(platfOrderList));
		} catch (Exception e) {
			logger.error("## 查询会员信息出错", e);
		}
		return mv;
	}
	
	/**
	 * 订单详情页面
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/getPlatfOrderGoodsDetails")
	public ModelAndView getPlatfOrderGoodsDetails(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("ecom/platforder/listPlatfordeDetails"); // 订单详情页面
		String orderId = req.getParameter("orderId");
		if (StringUtil.isNullOrEmpty(orderId)) {
			return null;
		}
		PlatfOrder po = new PlatfOrder();
		String memberId = StringUtil.nullToString(req.getSession().getAttribute(Constants.FRONT_MEMBER_ID_KEY));
		if (StringUtil.isNullOrEmpty(memberId)) {
			return null;
		}
		po.setMemberId(memberId);
		po.setOrderId(orderId);
		List<PlatfOrder> platfOrderList = platfOrderService.getPlatfOrderGoodsByMemberId(po);
		mv.addObject("platfOrderList", platfOrderList);
		OrderShip orderShip = orderShipService.getOrderShipByOrderId(orderId);
		mv.addObject("orderShip", orderShip);
		return mv;
	}

	/**
	 * 删除订单
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/deletePlatfOrder")
	public BaseResult<Object> deletePlatfOrder(HttpServletRequest req) {
		String orderId = req.getParameter("orderId");
		return platfOrderService.deletePlatfOrder(orderId);
	}
}



