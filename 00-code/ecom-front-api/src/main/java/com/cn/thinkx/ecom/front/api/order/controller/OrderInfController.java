package com.cn.thinkx.ecom.front.api.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cn.thinkx.ecom.common.util.AESUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.order.domain.OrderInf;
import com.cn.thinkx.ecom.front.api.order.service.OrderInfService;
import com.cn.thinkx.ecom.front.api.routes.domain.Routes;
import com.cn.thinkx.ecom.front.api.routes.service.RoutesService;

@RestController
@RequestMapping(value = "order")
public class OrderInfController {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderInfService orderInfService;

	@Autowired
	private RoutesService routesService;

	// 知了企服key
	@Value("${HKB_AES_KEY}")
	private String HKB_AES_KEY;

	// 海易通key
	@Value("${HYT_AES_KEY}")
	private String HYT_AES_KEY;

	// 家乐宝key
	@Value("${JLB_AES_KEY}")
	private String JLB_AES_KEY;

	// 嘉福京东key
	@Value("${JF_KEY}")
	private String JF_KEY;

	//嘉福京东对接参数
	@Value("${ident}")
	private String ident; //接入识别码

	@Value("${e_eid}")
	private String e_eid; //企业标识

	@Value("${order_service}")
	private String order_service; //服务接口

	/**
	 * 电商订单列表
	 * 
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/listOrderInf")
	public ModelAndView listOrderInf(HttpServletRequest req) {
		ModelAndView mv = null;
		// 接收知了企服参数进行解密
		String paramInfo = req.getParameter("paramInfo");
		if (StringUtil.isNullOrEmpty(paramInfo)) {
			logger.error("## 接收知了企服我的订单参数为空");
			return null; //TODO 统一返回错误页面 （接收参数为空）
		}
		String param = AESUtil.jzDecrypt(paramInfo, HKB_AES_KEY);
		logger.info("接收知了企服用户信息为[{}]", param);
		String[] str = param.split("\\|");//userId：str[0], mobile：str[1], openId：str[2]
		// 查询订单列表
		List<OrderInf> orderList = orderInfService.selectByOrderUserId(str[0]);
		if (orderList != null && orderList.size() > 0) {
			for (OrderInf o : orderList) {
				List<Routes> routesList = routesService.selectByEcomCode(o.getChannel());
				if (routesList.size() > 0) {
					o.setEcomLogo(routesList.get(0).getEcomLogo());
				}
			}
			mv = new ModelAndView("order/listOrderInf");;
			mv.addObject("orderList", orderList);
			mv.addObject("mobile", str[1]);
			mv.addObject("openID", str[2]);
		} else {
			mv = new ModelAndView("order/emptyOrderInf");
		}
		return mv;
	}

	/**
	 * 点击订单列表中的订单跳转第三方的订单详情页面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@PostMapping(value = "/viewOrder")
	public void viewOrder(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		String mobile = req.getParameter("mobile");
		String openID = req.getParameter("openID");
		logger.info("用户订单详情跳转前，接收的参数为，订单ID[{}]，手机号[{}]，微信号[{}]", id, mobile, openID);
		try {
			StringBuffer url = new StringBuffer();
			if (StringUtil.isNullOrEmpty(id)) {
				logger.info("★★★★★Request viewOrder get id is [Null]★★★★★");
				req.getRequestDispatcher("error.html").forward(req, resp);//TODO 统一返回错误页面（接收参数为空）
			}
			if (StringUtil.isNullOrEmpty(mobile)) {
				logger.info("★★★★★Request viewOrder get mobile is [Null]★★★★★");
				req.getRequestDispatcher("error.html").forward(req, resp);//TODO 统一返回错误页面（接收参数为空）
			}
			if (StringUtil.isNullOrEmpty(openID)) {
				logger.info("★★★★★Request viewOrder get openID is [Null]★★★★★");
				req.getRequestDispatcher("error.html").forward(req, resp);//TODO 统一返回错误页面（接收参数为空）
			}
			// 根据主键查询订单信息
			OrderInf order = orderInfService.selectByPrimaryKey(id);
			// 根据渠道号查询对应的订单链接
			List<Routes> routesList = routesService.selectByEcomCode(order.getChannel());
			if (routesList.size() > 0)
				order.setOrderUrl(routesList.get(0).getOrderUrl());
			// 将待解密参数按照第三方的key进行加密处理（按照海易通参数要求进行加密后的用户信息）
			String paramInfos = AESUtil.jzEncrypt(order.getRouterOrderNo(), HYT_AES_KEY);
			String timeStr = String.valueOf(System.currentTimeMillis());
			// 按照海易通参数要求进行加密后的时间戳
			String timeStamps = AESUtil.jzEncrypt(timeStr, HYT_AES_KEY); 
			//				logger.info("根据海意通的加密要求对参数进行加密后的订单信息参数为：[{}]，加密后的时间戳为：[{}]", paramInfos, timeStamps);
			// 根据家乐宝的要求对参数进行加密
			StringBuffer paramStr = new StringBuffer();
			paramStr.append(order.getUserId()).append("|").append(mobile).append("|").append(openID).append("|")
			.append(order.getMerchantNo()).append("|").append(order.getShopNo()).append("|")
			.append(order.getRouterOrderNo());
			//				logger.info("按照家乐宝的要求对订单信息拼接成待加密的字符串：[{}]", paramStr);
			// 按照家乐宝参数要求进行加密
			String param = AESUtil.jzEncrypt(paramStr.toString(), JLB_AES_KEY);
			// 跳转第三方电商url
			switch (order.getChannel()) {
			case "40006002":
				url.append(order.getOrderUrl()).append("&params=").append("{\"userInfo\":\"").append(param).append("\"}");
				break;
			case "40006001":
				url.append(order.getOrderUrl()).append("?paramInfo=").append(paramInfos).append("&timeStamp=").append(timeStamps);
				break;
			default:
				break;
			}
			resp.sendRedirect(url.toString());
		} catch (Exception e) {
			logger.error("查询订单列表信息出错", e);
		}
	}

}
