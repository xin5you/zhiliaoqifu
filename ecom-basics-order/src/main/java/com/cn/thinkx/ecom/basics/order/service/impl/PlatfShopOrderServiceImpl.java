package com.cn.thinkx.ecom.basics.order.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.order.domain.OrderRefund;
import com.cn.thinkx.ecom.basics.order.domain.PlatfOrder;
import com.cn.thinkx.ecom.basics.order.domain.PlatfShopOrder;
import com.cn.thinkx.ecom.basics.order.domain.ReturnsOrder;
import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.basics.order.mapper.OrderRefundMapper;
import com.cn.thinkx.ecom.basics.order.mapper.PlatfOrderMapper;
import com.cn.thinkx.ecom.basics.order.mapper.PlatfShopOrderMapper;
import com.cn.thinkx.ecom.basics.order.mapper.ReturnsOrderMapper;
import com.cn.thinkx.ecom.basics.order.mapper.SellbackGoodslistMapper;
import com.cn.thinkx.ecom.basics.order.req.PlatfOrderRefundReq;
import com.cn.thinkx.ecom.basics.order.resp.PlatfOrderRefundResp;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.common.constants.Constants;
import com.cn.thinkx.ecom.common.constants.Constants.ChannelEcomType;
import com.cn.thinkx.ecom.common.constants.Constants.refundFalg;
import com.cn.thinkx.ecom.common.constants.ExceptionEnum;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
import com.cn.thinkx.ecom.common.util.DateUtil;
import com.cn.thinkx.ecom.common.util.HttpClientUtil;
import com.cn.thinkx.ecom.common.util.SignUtil;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;

@Service("platfShopOrderService")
public class PlatfShopOrderServiceImpl extends BaseServiceImpl<PlatfShopOrder> implements PlatfShopOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfShopOrderMapper platfShopOrderMapper;

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private OrderRefundMapper orderRefundMapper;

	@Autowired
	private ReturnsOrderMapper returnsOrderMapper;

	@Autowired
	private PlatfOrderMapper platfOrderMapper;
	
	@Autowired
	private SellbackGoodslistMapper sellbackGoodslistMapper;

	@Override
	public List<PlatfShopOrder> getPlatfShopOrderList(PlatfShopOrder pso) {
		return platfShopOrderMapper.getPlatfShopOrderList(pso);
	}

	/**
	 * 修改二级订单状态
	 * 
	 * @param pso
	 */
	public void updatePlatfShopOrderStatus(PlatfShopOrder pso) {
		platfShopOrderMapper.updatePlatfShopOrderStatus(pso);
	}

	/**
	 * 查找二级订单 by 一级订单id and 二级订单状态
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	public List<PlatfShopOrder> getPlatfShopOrderByOIdAndStatus(String orderId, String status) {
		return platfShopOrderMapper.getPlatfShopOrderByOIdAndStatus(orderId, status);
	}

	@Override
	public List<PlatfShopOrder> getPlatfShopOrderLists(PlatfShopOrder pso) {
		return platfShopOrderMapper.getPlatfShopOrderLists(pso);
	}

	@Override
	public List<PlatfShopOrder> getPlatfShopOrderByOIdAndNotStatus(String orderId, String status) {
		return platfShopOrderMapper.getPlatfShopOrderByOIdAndNotStatus(orderId, status);
	}

	@Override
	public int updatePlatfShopOrder() {
		return platfShopOrderMapper.updatePlatfShopOrder();
	}

	/**
	 * 电商退款-（platfShopOrder：二级订单）
	 * 
	 */
	@Override
	public boolean doHkbStoreRefund(PlatfShopOrder platfShopOrder) {
		boolean falg = false;
		/** 调用退款接口 */
		try {
			String key = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					Constants.HKB_STORE_REFUND_KEY);
			// 通过二级订单id查询退款表中数据
			OrderRefund orderRefund = new OrderRefund();
			orderRefund.setsOrderId(platfShopOrder.getsOrderId());
			List<OrderRefund> orderRefundList = orderRefundMapper.getOrderRefundList(orderRefund);
			orderRefund = orderRefundList.get(0);

			PlatfOrderRefundReq req = new PlatfOrderRefundReq();
			req.setOriOrderId(platfShopOrder.getOrderId());
			req.setRefundOrderId(orderRefund.getRefundId());
			req.setRefundAmount(orderRefund.getRefundAmt()); // （退款金额-包括订单支付金额+配送费用）
			req.setChannel(ChannelEcomType.CEU06.getCode());
			req.setRefundFlag(refundFalg.refundFalg2.getCode());
			req.setTimestamp(System.currentTimeMillis());
			req.setSign(SignUtil.genSign(req, key));
			String url = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,
					Constants.HKB_STORE_REFUND_URL);
			logger.info("## 自建商城退款请求参数，[{}]", JSONObject.toJSONString(req));
			String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(req));// 发起退款请求
			logger.info("## 自建商城退款返回参数[{}]", result);
			PlatfOrderRefundResp resp = JSONObject.parseObject(result, PlatfOrderRefundResp.class);
			if (ExceptionEnum.SUCCESS_CODE.equals(resp.getCode())) {
				orderRefund.setRefundStatus(Constants.RefundStatus.RFS1.getCode());
				orderRefund.setDmsRelatedKey(resp.getTxnFlowNo());
				orderRefund.setRefundTime(DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
				orderRefundMapper.updateByPrimaryKey(orderRefund);

				/** 订单退换货申请表 */
				ReturnsOrder returnsOrder = returnsOrderMapper.selectByPrimaryKey(orderRefund.getReturnsId());
				returnsOrder.setReturnsStatus(Constants.ReturnsStatus.RS9.getCode()); // 审核状态：已完成
				returnsOrderMapper.updateByPrimaryKey(returnsOrder);

				/** 售后退货时退款，修改退货商品表 */
				if(Constants.ReturnsType.RT1.getCode().equals(returnsOrder.getReturnsType())){
					SellbackGoodslist sellbackGoodslist = new SellbackGoodslist();
					sellbackGoodslist.setReturnsId(orderRefund.getReturnsId());
					sellbackGoodslist.setApplyReturnState(Constants.ApplyReturnState.ARS4.getCode());	//退货状态标识：已退款
					sellbackGoodslistMapper.updateSellbackGoodslistByReturnsId(sellbackGoodslist);
				}
				
				/** 修改一级订单的状态 */
				PlatfShopOrder pso = new PlatfShopOrder();
				pso.setOrderId(platfShopOrder.getOrderId());
				List<PlatfShopOrder> psoList = platfShopOrderMapper.getPlatfShopOrderList(pso);
				logger.info("## 查询需要退款的一级订单OrderId:[{}]下的所有二级订单:[{}]", platfShopOrder.getOrderId(),
						JSON.toJSONString(psoList));
				if (psoList != null && psoList.size() > 0) {
					PlatfOrder po = new PlatfOrder();
					po.setOrderId(platfShopOrder.getOrderId());
					// 判断退款申请表中一级订单下的所有订单是否都已退款
					int flag = 0; // 跳出循环标识
					for (PlatfShopOrder platfShopOrder2 : psoList) {
						List<ReturnsOrder> roList = returnsOrderMapper
								.getReturnsOrderBySorderId(platfShopOrder2.getsOrderId());
						logger.info("## 申请退款的订单申请信息:[{}]--------->查询二级订单号sOrderId:[{}]", JSON.toJSONString(roList),
								platfShopOrder2.getsOrderId());
						if (flag == 1) {
							break;
						}
						if (roList != null && roList.size() > 0) {
							for (ReturnsOrder ro : roList) {
								if (StringUtil.isNullOrEmpty(ro)) {
									po.setPayStatus(Constants.PlatfOrderPayStat.PayStat04.getCode()); // 部分退款
									flag = 1;
									break;
								} else {
									logger.info("## 退款申请表中二级门店订单号sOrderId：[{}]-----------ReturnsStatus：[{}]",
											ro.getsOrderId(), ro.getReturnsStatus());
									if (Constants.ReturnsStatus.RS9.getCode().equals(ro.getReturnsStatus())) { // 判断是否已退款
										po.setPayStatus(Constants.PlatfOrderPayStat.PayStat03.getCode()); // 已退款
									} else {
										po.setPayStatus(Constants.PlatfOrderPayStat.PayStat04.getCode()); // 部分退款
										flag = 1;
										break;
									}
								}
							}
						}
					}
					platfOrderMapper.updateByPrimaryKey(po);
				}
				falg = true;
			} else {
				OrderRefund order =orderRefundMapper.selectByPrimaryKey(orderRefund.getRefundId());
				if(!Constants.RefundStatus.RFS1.getCode().equals(order.getRefundStatus())){
					orderRefund.setRefundStatus(Constants.RefundStatus.RFS2.getCode());
					orderRefundMapper.updateByPrimaryKey(orderRefund);
					falg = false;
				}else{
					falg = true;
				}
			}
		} catch (Exception e) {
			logger.error("## 自建电商退款异常", e);
		}
		return falg;
	}
}
