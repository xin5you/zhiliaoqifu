package com.cn.thinkx.ecom.front;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.cn.thinkx.ecom.FrontApp;
import com.cn.thinkx.ecom.basics.order.service.CartService;
import com.cn.thinkx.ecom.basics.order.service.ExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderExpressPlatfService;
import com.cn.thinkx.ecom.basics.order.service.OrderProductItemService;
import com.cn.thinkx.ecom.basics.order.service.OrderRefundService;
import com.cn.thinkx.ecom.basics.order.service.OrderShipService;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.basics.order.service.ReturnsOrderService;
import com.cn.thinkx.ecom.basics.order.service.SellbackGoodslistService;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.ecom.you163.api.meta.JsonResult;
import com.cn.thinkx.ecom.you163.api.service.YXOpenApiService;
import com.cn.thinkx.ecom.you163.api.service.YXOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrontApp.class)// 指定spring-boot的启动类
public class YxOrderTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CartService cartService;
	
	@Autowired
	private PlatfOrderService platfOrderService;
	
	@Autowired
	private PlatfShopOrderService platfShopOrderService;
	
	@Autowired
	private OrderShipService orderShipService;
	
	
	@Autowired
	private OrderExpressPlatfService orderExpressPlatfService;
	
	@Autowired
	private ExpressPlatfService expressPlatfService;
	
	@Autowired
	private YXOpenApiService yxOpenApiService;
	
	@Autowired
	private OrderProductItemService orderProductItemService;
	
	@Autowired
	private YXOrderService yxOrderService; 
	
	@Autowired
	private OrderRefundService orderRefundService;
	
	@Autowired
	private ReturnsOrderService returnsOrderService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	
	@Autowired
	private SellbackGoodslistService sellbackGoodslistService;

	
	/**
	 * 下单
	 * @param sOrderId
	 * @throws Exception
	 */
//    @Test
//    public void doCreateOrder() throws Exception{
//		
//    	String sOrderId="100014";
//    	PlatfShopOrder platfShopOrder=platfShopOrderService.selectByPrimaryKey(sOrderId);
//		
//		/**
//		 * 查找订单的收货地址
//		 */
//		OrderShip orderShip=orderShipService.getOrderShipByOrderId(platfShopOrder.getOrderId());
//	 	OrderVO orderVO = new OrderVO();
//        orderVO.setOrderId(sOrderId); //二级订单号
//        orderVO.setSubmitTime(DateUtils.parseDateToString(DateUtils.addMinute(new Date(),-2), null)); //用户下单时间
//        orderVO.setPayTime(DateUtils.parseDateToString(DateUtils.addMinute(new Date(),-1), null)); //用户的支付时间
//        orderVO.setReceiverName(orderShip.getShipName()); //收件人姓名
//        orderVO.setReceiverMobile(orderShip.getShipMobile()); //收件人手机号
//        orderVO.setReceiverPhone(orderShip.getShipMobile());
//        orderVO.setReceiverProvinceName(orderShip.getShipProvinceName()); //省
//        orderVO.setReceiverCityName(orderShip.getShipCityName());//市
//        orderVO.setReceiverDistrictName(orderShip.getShipRegionName());//区（县）
//        orderVO.setReceiverAddressDetail(orderShip.getShipAddr()); //详细地址
//        BigDecimal realPrice=NumberUtils.RMBCentToYuanForDecimal(platfShopOrder.getPayAmt());  //订单金额
//        BigDecimal expFee=NumberUtils.RMBCentToYuanForDecimal(platfShopOrder.getShippingFreightPrice()); //配送费用
//        orderVO.setRealPrice(NumberUtils.RMBaddMoneyToDecimal(realPrice, expFee)); //订单金额 包含邮费
//        orderVO.setExpFee(expFee); //运费金额
//
//        orderVO.setPayMethod("HKBPAY");
//        //订单SKU列表
//        List<OrderSkuVO> orderSkus = new ArrayList<OrderSkuVO>();
//       
//        //查询订单的货品明细
//        OrderProductItem respItems=new OrderProductItem();
//        respItems.setsOrderId(sOrderId);
//        List<OrderProductItem> orderSkuItmes= orderProductItemService.getOrderProductItemList(respItems);
//        
//        OrderSkuVO orderSku=null;
//        //遍历订单明细
//        for(OrderProductItem productItem:orderSkuItmes){
//        	orderSku=new OrderSkuVO();
//        	orderSku.setSkuId(productItem.getSkuCode());
//        	orderSku.setProductName(productItem.getGoodsName());
//        	orderSku.setSaleCount(Integer.parseInt(productItem.getProductNum()));//货品数量
//        	orderSku.setOriginPrice(new BigDecimal(NumberUtils.RMBCentToYuan(productItem.getProductPrice()))); //货品售价
//        	orderSku.setSubtotalAmount(NumberUtils.getSubtotalAmountYUAN(productItem.getProductPrice(),productItem.getProductNum())); //小计
//        	orderSkus.add(orderSku);
//        }
//        orderVO.setOrderSkus(orderSkus);
//        
//        /**
//         * 网易优选下单操作
//         */
//        JsonResult jsonResult= yxOpenApiService.handlePayedOrder(orderVO);
//        if("200".equals(jsonResult.getCode())){
//        	OrderOutVO orderOut=JSONArray.parseObject(jsonResult.getResult().toString(), OrderOutVO.class);
//        	yxOrderService.doOrderPackageStates(orderOut);
//        }
//	}
//    
	/**
	 * 取消订单
	 * 
	 */
//    @Test
//    public void cancelOrder(){
//    	JsonResult jr = null;
//    	String sOrderId="126600000358";
//		try {
//			if(!StringUtil.isNullOrEmpty(sOrderId)){ // 二级订单号（渠道订单号）
//				PlatfShopOrder pso = platfShopOrderService.selectByPrimaryKey(sOrderId);
//				if (!StringUtil.isNullOrEmpty(pso)) {
//					if (Constants.SubOrderStatus.SOS00.getCode().equals(pso.getSubOrderStatus())
//							|| Constants.SubOrderStatus.SOS10.getCode().equals(pso.getSubOrderStatus())) {
//						/** 订单退换货申请表 */
//						ReturnsOrder returnsOrder = new ReturnsOrder();
//						returnsOrder.setsOrderId(pso.getsOrderId());
//						returnsOrder.setMemberId(pso.getMemberId());
//						returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS0.getCode()); // 未处理
//						returnsOrder.setReturnsType(Constants.ReturnsType.RT3.getCode()); // 用户申请取消
//						returnsOrder.setAddTime("");
//						returnsOrderService.insert(returnsOrder);
//
//						/** 订单货品明细表 */
//						OrderProductItem opi = new OrderProductItem();
//						opi.setsOrderId(pso.getsOrderId());
//						List<OrderProductItem> orderProductItemList = orderProductItemService
//								.getOrderProductItemList(opi);
//
//						/** 退货商品表 */
//						for (OrderProductItem orderProductItem : orderProductItemList) {
//							SellbackGoodslist sg = new SellbackGoodslist();
//							sg.setsOrderId(pso.getsOrderId());
//							sg.setReturnsId(returnsOrder.getReturnsId());
//							sg.setoItemId(orderProductItem.getoItemId());
//							sg.setShipNum(orderProductItem.getProductNum());
//							sg.setReturnNum(orderProductItem.getProductNum());
//							sellbackGoodslistService.insert(sg);
//						}
//
//						if (Constants.GoodsEcomCodeType.ECOM01.getCode().equals(pso.getEcomCode())) {
//							// 调用严选渠道取消订单接口
//							jr = yxOpenApiService.handleCancelOrder(sOrderId);
//							logger.info("## 调用严选用户取消订单接口返回值:[{}]----->订单号：[{}]", jr, sOrderId);
//							if ("200".equals(jr.getCode())) {
//								OrderCancelResult orderCancelResult = JSONArray.parseObject(jr.getResult().toString(),
//										OrderCancelResult.class);
//								switch (orderCancelResult.getCancelStatus()) {
//								case 0: // 0:不允许取消
//									/** 门店订单表 */
//									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS28.getCode()); // 取消被拒
//									if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
//										/** 订单退换货申请表 */
//										returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS1.getCode()); // 审核状态：已拒绝
//										returnsOrderService.updateByPrimaryKey(returnsOrder);
//									}
//									break;
//								case 1: // 允许取消
//									/** 门店订单表 */
//									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS27.getCode()); // 已取消
//									if (platfShopOrderService.updateByPrimaryKey(pso) > 0) {
//										// 判断一级订单下的所有二级订单是否都是已取消状态
//										List<PlatfShopOrder> psoList = platfShopOrderService
//												.getPlatfShopOrderByOIdAndNotStatus(pso.getOrderId(),
//														Constants.SubOrderStatus.SOS27.getCode());
//										if (psoList.size() == 0) {
//											PlatfOrder po = new PlatfOrder();
//											po.setOrderId(pso.getOrderId());
//											po.setPayStatus(Constants.PlatfOrderPayStat.PayStat08.getCode()); // 已取消
//											platfOrderService.updateByPrimaryKey(po);
//										}
//										/** 订单退换货申请表 */
//										returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS2.getCode()); // 审核状态：已同意
//										if (returnsOrderService.updateByPrimaryKey(returnsOrder) > 0) {
//											/** 退款表 */
//											OrderRefund or = new OrderRefund();
//											or.setReturnsId(returnsOrder.getReturnsId());
//											orderRefundService.saveOrderRefundInf(returnsOrder.getReturnsId(), pso);
//											/** 调用退款接口 */
//											this.doHkbStoreRefund(pso);
//										}
//									}
//									break;
//								case 2: // 待审核
//									pso.setSubOrderStatus(Constants.SubOrderStatus.SOS26.getCode()); // 申请取消待审核
//									platfShopOrderService.updateByPrimaryKey(pso);
//									break;
//								default:
//									break;
//								}
//							}
//						}
//					}
//				}
//
//			}
//		} catch (Exception e) {
////			logger.error("## 渠道取消订单接口失败",e);
//		}
//	
//    }
    
//    public void doHkbStoreRefund(PlatfShopOrder platfShopOrder) {
//		logger.info("## 进入电商退款");
//		/** 调用退款接口 */
//		try {
//			String key = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.HKB_STORE_REFUND_KEY);
//			//通过二级订单id查询退款表中数据
//			OrderRefund orderRefund = new OrderRefund();
//			orderRefund.setsOrderId(platfShopOrder.getsOrderId());
//			List<OrderRefund> orderRefundList = orderRefundService.getOrderRefundList(orderRefund);
//			orderRefund = orderRefundList.get(0);
//			
//			PlatfOrderRefundReq req = new PlatfOrderRefundReq();
//			req.setOriOrderId(platfShopOrder.getOrderId());
//			req.setRefundOrderId(orderRefund.getRefundId());
//			req.setRefundAmount(orderRefund.getRefundAmt()); // （退款金额-包括订单支付金额+配送费用）
//			req.setChannel(ChannelCode.CHANNEL6.toString());
//			req.setRefundFlag(refundFalg.refundFalg2.getCode());
//			req.setTimestamp(System.currentTimeMillis());
//			req.setSign(SignUtil.genSign(req, key));
//			String url = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, Constants.HKB_STORE_REFUND_URL);
//			logger.info("## 自建商城退款请求参数，[{}]", JSONObject.toJSONString(req));
//			String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(req));// 发起退款请求
//			logger.info("## 自建商城退款返回参数[{}]", result);
//			PlatfOrderRefundResp resp = JSONObject.parseObject(result, PlatfOrderRefundResp.class);
//			if (ExceptionEnum.SUCCESS_CODE.equals(resp.getCode())) {
//				orderRefund.setRefundStatus(Constants.RefundStatus.RFS1.getCode());
//				orderRefund.setDmsRelatedKey(resp.getTxnFlowNo());
//			} else {
//				orderRefund.setRefundStatus(Constants.RefundStatus.RFS2.getCode());
//			}
//			/** 退款表 */
//			if (orderRefundService.updateByPrimaryKey(orderRefund) > 0) { // 更新退款表信息(已退款)
//				/** 订单退换货申请表 */
//				ReturnsOrder returnsOrder = new ReturnsOrder();
//				returnsOrder.setReturnsId(orderRefund.getReturnsId());
//				returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS9.getCode()); // 审核状态：已完成
//				returnsOrderService.updateByPrimaryKey(returnsOrder);
//			}
//
//		} catch (Exception e) {
//			logger.error("## 自建电商退款异常", e);
//		}
//	}
	
	@Test
	public void orderYx(){
		try {
			String sOrderId = "126600000375";
			JsonResult jr = null;
			//（严选取消订单）异常取消订单回调
			jr = yxOpenApiService.handleCallbackCancelOrder(sOrderId);
			
//			jr = yxOpenApiService.handleCallbackDeliveryOrder(sOrderId);
			logger.info("## jr:[{}]",JSON.toJSONString(jr));
		} catch (Exception e) {
			logger.error("## 异常", e);
		}
	}
    
}
