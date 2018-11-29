package com.cn.thinkx.ecom.front.api.platforder.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.basics.order.domain.OrderDeliveryInfo;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.platforder.service.GoodsOrderService;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;

@RestController
@RequestMapping(value = "ecom/order")
public class GoodsOrderController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	// @Autowired
	// private YXOpenApiService yxOpenApiService;

	@Autowired
	private GoodsOrderService goodsOrderService;

	/**
	 * 渠道门店订单
	 */
	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private ExpressPlatfService expressPlatfService;

	/**
	 * 渠道下单接口(yanxuan.order.paid.create)
	 * 
	 * @param orderVO
	 *            订单 JSON字符串
	 * @return
	 */
	// @GetMapping("/payedOrder")
	// public JsonResult payedOrder(OrderVO orderVO) {
	// return yxOpenApiService.handlePayedOrder(orderVO);
	// }
	//
	/**
	 * 用户取消订单接口(yanxuan.order.paid.cancel)
	 * 
	 * @param sOrderId
	 *            二级订单号
	 * @return
	 */
	@PostMapping(value="/cancelOrder")
	public JsonResult cancelOrder(@RequestParam(value = "sOrderId") String sOrderId) {
		return goodsOrderService.cancelOrder(sOrderId);
	}

	/**
	 * 订单确认收货接口(yanxuan.order.received.confirm)
	 * 
	 * @param orderId
	 *            订单号
	 * @return
	 */
	@PostMapping(value="/confirmOrder")
	public JsonResult confirmOrder(@RequestParam(value = "sOrderId") String sOrderId) {
		return goodsOrderService.confirmOrder(sOrderId);
	}

	/**
	 * 渠道订单信息查询接口(yanxuan.order.paid.get)
	 * 
	 * @param orderId
	 *            订单号
	 * @return
	 */
	// @GetMapping("/getOrder")
	// public JsonResult getOrder(
	// @RequestParam(value = "orderId") String orderId) {
	// return yxOpenApiService.handleGetOrder(orderId);
	// }

	/**
	 * 获取订单包裹列表
	 * 
	 * @param request
	 * @param sOrderId
	 */
	public void getOrderPackageList(HttpServletRequest request, @RequestParam(value = "sOrderId") String sOrderId) {

	}

	/**
	 * 获取物流轨迹信息接口(yanxuan.order.express.get)
	 * 
	 * @param sOrderId
	 *            订单号 //二级门店订单号
	 * @return
	 */
	@GetMapping("/getExpress")
	public ModelAndView getExpress(HttpServletRequest request, @RequestParam(value = "sOrderId") String sOrderId) {
		ModelAndView mv = new ModelAndView("ecom/platforder/lookLogistics");
		if (StringUtil.isNullOrEmpty(sOrderId)) {
			return null;
		}
		try {
			// 门店二级订单
			PlatfShopOrder platfShopOrder = platfShopOrderService.selectByPrimaryKey(sOrderId);
			if (platfShopOrder != null) {
				ExpressPlatf ep = new ExpressPlatf();
				ep.setsOrderId(sOrderId);
				List<ExpressPlatf> expressPlatfList = expressPlatfService.getOrderExpressPlatfBySOrderId(sOrderId); // 获取该订单下得所有包裹
				for (ExpressPlatf expressPlatf : expressPlatfList) {
					expressPlatf.setPackageStat(StringUtil.nullToString(
							Constants.PackageStatus.findByCode(expressPlatf.getPackageStat()).getValue()));
					List<ExpressPlatf> expressPlatfProList = expressPlatfService
							.getExpressPlatfProductByPackId(expressPlatf.getPackId()); // 包裹得货品信息
					List<OrderProductItem> orderProductItems = new ArrayList<>();
					for (ExpressPlatf expressPlatf2 : expressPlatfProList) {
						OrderProductItem item = new OrderProductItem();
						item.setPicUrl(expressPlatf2.getPicUrl()); // 货品图片
						orderProductItems.add(item);
					}
					expressPlatf.setOrderProductItem(orderProductItems); // 货品信息
					OrderDeliveryInfo orderDeliveryInfo = goodsOrderService.getOrderExpress(platfShopOrder,
							expressPlatf.getPackId());
					if (!StringUtil.isNullOrEmpty(orderDeliveryInfo)) {
						expressPlatf.setOrderDeliveryInfo(orderDeliveryInfo); // 物流轨迹信息
					}
				}
				mv.addObject("expressPlatfList", expressPlatfList);
			}
		} catch (Exception e) {
			logger.error("##获取订单物流轨迹 error:[{}]", e);
		}
		return mv;
	}

	/**
	 * 渠道自助注册回调(yanxuan.callback.method.register) 渠道通过这个请求告知严选需要通知的回调方法
	 * 
	 * @param methods
	 *            <I>需注册的回调方法名</I><br>
	 *            <br>
	 *            <I>多个方法名用英文逗号分隔，覆盖原来所有方法(包括默认的方法)</I><br>
	 *            <br>
	 * @return
	 */
	// @RequestMapping("/registerCallbackMethod")
	// public JsonResult registerCallbackMethod(@RequestParam(value = "methods")
	// final String methods) {
	// return yxOpenApiService.handleRegisterCallbackMethod(methods);
	// }

}
