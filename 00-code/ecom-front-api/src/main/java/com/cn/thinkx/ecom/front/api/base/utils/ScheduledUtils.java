package com.cn.thinkx.ecom.front.api.base.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.order.service.PlatfOrderService;
import com.cn.thinkx.ecom.basics.order.service.PlatfShopOrderService;
import com.cn.thinkx.ecom.bm001.api.constants.BMConstants;
import com.cn.thinkx.ecom.bm001.api.service.BMOpenApiService;
import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeChannelType;
import com.cn.thinkx.ecom.common.constants.Constants.PhoneRechargeTransStat;
import com.cn.thinkx.ecom.common.constants.Constants.RechargeState;
import com.cn.thinkx.ecom.front.api.phoneRecharge.domain.PhoneRechargeOrder;
import com.cn.thinkx.ecom.front.api.phoneRecharge.service.PhoneRechargeService;
import com.cn.thinkx.ecom.front.api.platforder.service.GoodsOrderService;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;

@Component
public class ScheduledUtils {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlatfOrderService platfOrderService;
	
	@Autowired
	private PlatfShopOrderService platfShopOrderService;

	@Autowired
	private GoodsOrderService goodsOrderService;
	
//	@Autowired
//	private SolrGoodsService solrGoodsService;
	
	@Autowired
	private PhoneRechargeService phoneRechargeService;
	
	@Autowired
	private BMOpenApiService bmOpenApiService;

	@Autowired
	 private JedisClusterUtils jedisClusterUtils;
	
	/**
	 * 定时刷新确认收货的订单数据（凌晨0点启动）
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void confirmOrder() {
		try {
//			logger.info("## 定时刷新自动确认收货***************doRefreshPlatOrderInf****触发时间：[{}]",DateUtil.getCurrentDateStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
			goodsOrderService.doRefreshPlatOrderInf();
		} catch (Exception e) {
			logger.error("## 自动确认收货出错", e);
		}
	}

	@Scheduled(cron = "0 0/5 * * * ?")
	public void updatePlatfOrder() {
		try {
			platfOrderService.updateOlatfOrder();
			platfShopOrderService.updatePlatfShopOrder();
		} catch (Exception e) {
			logger.error("定时更新订单出错", e);
		}
	}
	
	/**
	 * 定时刷新slorGoods信息
	 * 
	 * 每天早上6点，下午6点
	 */
//	@Scheduled(cron = "0 0 6,18 * * ?")
//	public void doRefreshSlorGoods(){
//		try {
//			solrGoodsService.addGoodsBatch();
//		} catch (Exception e) {
//			logger.error("## 定时刷新slorGoods信息出错", e);
//		}
//	}
	
	/**
	 * 定时查询手机充值订单数据在充值中的数据
	 * (每一分钟调用一次)
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void updatePhoneRecharge() {
		try {
			//查询一分钟以上十分钟以内的充值中的手机充值订单
			List<PhoneRechargeOrder> list = phoneRechargeService.getPhoneRechargeOrderNotSuccess();
			if (list != null && list.size() > 0) {
				PhoneRechargeOrder rechargeOrde = null;
				String accessToken = null;
				for (PhoneRechargeOrder pro : list) {
					if (PhoneRechargeChannelType.PRC1001.getCode().equals(pro.getReqChannel())) {
						accessToken = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, BMConstants.BM_ACCESS_TOKEN);
					} else {
						accessToken = pro.getResv1();
					}
					//调用供应商订单查询接口
					BmOrderCustomGetResponse resp = bmOpenApiService.handleGetCustomOrder(pro.getrId(), accessToken);
					logger.info("话费订单查询，返回参数[{}]",JSONObject.toJSONString(resp));
					if(resp.isSuccess()){
						rechargeOrde = new PhoneRechargeOrder();
						rechargeOrde.setrId(resp.getOrderDetailInfo().getOuterTid());
						if(RechargeState.RechargeState09.getCode().equals(resp.getOrderDetailInfo().getRechargeState())){
							rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat3.getCode());
							phoneRechargeService.doPhoneRechargeOrder(rechargeOrde);
						}
						if(RechargeState.RechargeState01.getCode().equals(resp.getOrderDetailInfo().getRechargeState())){
							rechargeOrde.setTransStat(PhoneRechargeTransStat.TransStat2.getCode());
							phoneRechargeService.doPhoneRechargeOrder(rechargeOrde);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(" ## 定时更新手机充值订单出错", e);
		}
	}
}
