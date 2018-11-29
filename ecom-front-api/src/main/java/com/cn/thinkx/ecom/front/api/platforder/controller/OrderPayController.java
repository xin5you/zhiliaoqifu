package com.cn.thinkx.ecom.front.api.platforder.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.service.MemberInfService;
import com.cn.thinkx.ecom.basics.order.domain.OrderProductItem;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelEcomType;
import com.cn.thinkx.ecom.common.constants.Constants.PlatfOrderPayStat;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.ResultsUtil;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.platforder.domain.OrderBack;
import com.cn.thinkx.ecom.front.api.platforder.domain.OrderRedirect;
import com.cn.thinkx.ecom.front.api.platforder.domain.OrderUnified;
import com.cn.thinkx.ecom.front.api.platforder.service.GoodsOrderService;
import com.cn.thinkx.ecom.front.api.platforder.service.OrderPayService;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;

@RestController
@RequestMapping(value = "goods/orderPay")
public class OrderPayController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MemberInfService memberInfService;
	
	@Autowired
	private PlatfOrderService platfOrderService;
	
	@Autowired
	private OrderProductItemService orderProductItemService;
	
	@Autowired
	private OrderPayService orderPayService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	
	@Qualifier("goodsOrderService")
	@Autowired
	private GoodsOrderService goodsOrderService;
	
	@Autowired
	private GoodsProductService goodsProductService;

	@Value("${CASH_URL}")
	private String CASH_URL;
	
	@Value("${NOTIFY_URL}")
	private String NOTIFY_URL;
	
	@Value("${REDIRECT_URL}")
	private String REDIRECT_URL;
	
	@Value("${ECOM_SING_KEY}")
	private String ECOM_SING_KEY;
	
	
	@RequestMapping(value = "/orderPay")
	public String orderPay(HttpServletRequest req, HttpServletResponse resp) {
		PlatfOrder platf = orderPayService.orderPay(req, resp);
		try {
			unifiedOrder(req, resp, platf);
		} catch (Exception e) {
			logger.error("跳转收银台出错", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/payMyOrder")
	public String payMyOrder(HttpServletRequest req, HttpServletResponse resp) {
		String orderId = req.getParameter("orderId");
		try {
			PlatfOrder platf = orderPayService.getPlatfOrderByPrimaryKey(orderId);
			unifiedOrder(req, resp, platf);
		} catch (Exception e) {
			logger.error("跳转收银台出错", e);
		}
		return null;
	}

    /**
	 * 调用知了企服电商平台统一下单接口
	 * @param req
	 * @param resp
	 * @return
	 */
	public String unifiedOrder(HttpServletRequest req, HttpServletResponse resp,PlatfOrder platf) {
		String innerMerchantNo = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_MCHNT_NO);
		String innerShopNo = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.ACC_HKB_SHOP_NO);
		String commodityName = "电商购物";//req.getParameter("commodityName");
		String commodityNum = "1";//req.getParameter("commodityNum");
		String attach = req.getParameter("attach");
		String redirect_type = "1";//req.getParameter("redirect_type");
		
		MemberInf memberInf = null;
		try {
			memberInf = memberInfService.selectByPrimaryKey(platf.getMemberId());
		} catch (Exception e) {
			logger.error("查询会员信息出错",e);
		}
		OrderUnified order = new OrderUnified();
		order.setChannel(ChannelEcomType.CEU06.getCode());
		order.setUserId(memberInf.getUserId());
		order.setOrderId(platf.getOrderId());
		order.setInnerMerchantNo(innerMerchantNo);
		order.setInnerShopNo(innerShopNo);
		order.setCommodityName(commodityName);
		order.setCommodityNum(commodityNum);
		order.setTxnAmount(platf.getPayAmt());
		order.setAttach(attach);
		order.setNotify_url(NOTIFY_URL);
		order.setRedirect_type(redirect_type);
		order.setRedirect_url(REDIRECT_URL);
		order.setSign(SignUtil.genSign(order, ECOM_SING_KEY));

		try {
			// 跳转知了企服收银台
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<form name='cashSubmit' method='post'  action='" + CASH_URL + "' >");
			if (!StringUtil.isNullOrEmpty(order.getAttach()))
				out.println("<input type='hidden' name='attach' value='" + order.getAttach() + "'>");
			if (!StringUtil.isNullOrEmpty(order.getRedirect_type()))
				out.println("<input type='hidden' name='redirect_type' value='" + order.getRedirect_type() + "'>");
			if (!StringUtil.isNullOrEmpty(order.getRedirect_url()))
				out.println("<input type='hidden' name='redirect_url' value='" + order.getRedirect_url() + "'>");
			out.println("<input type='hidden' name='channel' value='" + order.getChannel() + "'>");
			out.println("<input type='hidden' name='commodityName' value='" + order.getCommodityName() + "'>");
			out.println("<input type='hidden' name='commodityNum' value='" + order.getCommodityNum() + "'>");
			out.println("<input type='hidden' name='innerMerchantNo' value='" + order.getInnerMerchantNo() + "'>");
			out.println("<input type='hidden' name='innerShopNo' value='" + order.getInnerShopNo() + "'>");
			out.println("<input type='hidden' name='notify_url' value='" + order.getNotify_url() + "'>");
			out.println("<input type='hidden' name='orderId' value='" + order.getOrderId() + "'>");
			out.println("<input type='hidden' name='sign' value='" + order.getSign() + "'>");
			out.println("<input type='hidden' name='txnAmount' value='" + order.getTxnAmount() + "'>");
			out.println("<input type='hidden' name='userId' value='" + order.getUserId() + "'>");
			out.println("</form>");
			out.println("<script type='text/javascript'>");
			out.println("document.cashSubmit.submit()");
			out.println("</script>");
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * 重定向接口
	 * @param or
	 * @return
	 */
	@RequestMapping(value = "/redirectResult")
	public ModelAndView redirectResult(OrderRedirect or) {
		String payStatus = "";
		ModelAndView mv = new ModelAndView("redirect:/goods/platfOrder/getPlatfOrderGoodsByMemberId?payStatus"+payStatus);
		return mv;
	}

	/**
	 * 
	 * 支付成功回掉接口
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/backResult")
	public BaseResult backResult(HttpServletRequest req) {
		String channel = req.getParameter("channel");
		String respResult = req.getParameter("respResult");
		String innerMerchantNo = req.getParameter("innerMerchantNo");
		String innerShopNo = req.getParameter("innerShopNo");
		String userId = req.getParameter("userId");
		String orderId = req.getParameter("orderId");
		String settleDate = req.getParameter("settleDate");
		String txnFlowNo = req.getParameter("txnFlowNo");
		String oriTxnAmount = req.getParameter("oriTxnAmount");
		String txnAmount = req.getParameter("txnAmount");
		String attach = req.getParameter("attach");
		String sign = req.getParameter("sign");

		OrderBack orderBack = new OrderBack();
		orderBack.setChannel(channel);
		orderBack.setRespResult(respResult);
		orderBack.setInnerMerchantNo(innerMerchantNo);
		orderBack.setInnerShopNo(innerShopNo);
		orderBack.setUserId(userId);
		orderBack.setOrderId(orderId);
		orderBack.setSettleDate(settleDate);
		orderBack.setTxnFlowNo(txnFlowNo);
		orderBack.setOriTxnAmount(oriTxnAmount);
		orderBack.setTxnAmount(txnAmount);
		orderBack.setAttach(attach);
		orderBack.setSign(sign);
		
		logger.info("回调返回参数------->>[{}]",JSONObject.toJSONString(orderBack));
		String checkSign = SignUtil.genSign(orderBack, ECOM_SING_KEY);
		if (!sign.equals(checkSign)) {
			return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
		}
		try {
			if(Constants.HKB_FAIL.equals(respResult)){
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			
			PlatfOrder order = platfOrderService.selectByPrimaryKey(orderId);
			
			if(order==null){
				return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
			}
			order.setDmsRelatedKey(txnFlowNo);
			order.setPayStatus(PlatfOrderPayStat.PayStat02.getCode());
			order.setPayTime(DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
			platfOrderService.updateByPrimaryKey(order); //修改订单支付状态
			
			List<OrderProductItem> itemList = orderProductItemService.getOrderProductItemByOrderId(orderId);
			
			GoodsProduct product ;
			for (OrderProductItem item : itemList) {
				product = new GoodsProduct();
				product.setProductId(item.getProductId());
				product.setIsStore(Long.valueOf(item.getProductNum()));
				int i = goodsProductService.updateGoodsProductIsStore(product);
				if (i != 1) {
					return ResultsUtil.error(ExceptionEnum.ERROR_CODE, ExceptionEnum.ERROR_MSG);
				}
			}
			
			//TODO渠道订单下单
			goodsOrderService.createEcomGoodsOrder(order);
			
		} catch (Exception e) {
			logger.error("渠道订单下单异常-->",e);
		}
		
		return ResultsUtil.success();
	}
}
