package com.ebeijia.zl.service.telrecharge.listener;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;


import com.ebeijia.zl.api.bm001.api.req.PayBillReq;
import com.ebeijia.zl.api.bm001.api.service.BMOpenApiService;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.service.telrecharge.service.ProviderInfService;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlOrderInfService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisCluster;

/**
 * 立方话费充值
 * 
 * @author zhuqiuyou
 * 
 * @since 2018-07-10 11:21:23
 *  
 */
@Component
public class BMRechargeMobileSessionAwareMessageListener implements MessageListenerConcurrently {
	
	private Logger logger = LoggerFactory.getLogger(BMRechargeMobileSessionAwareMessageListener.class);
	
	@Autowired
	private ProviderOrderInfService providerOrderInfService;

	@Autowired
	private ProviderInfService providerInfService;

	@Autowired
	private RetailChnlOrderInfService retailChnlOrderInfService;
	
	@Autowired
	private RetailChnlInfService retailChnlInfService;


	@Autowired
	private BMOpenApiService bmOpenApiService;

	@Autowired
	private JedisCluster  jedisCluster;

	/**
	 *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
	 *  不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
	 */
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		if(CollectionUtils.isEmpty(msgs)){
			//接受到的消息为空，不处理，直接返回成功
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		MessageExt messageExt = msgs.get(0);
		logger.info("接受到的消息为："+messageExt.toString());

		if(messageExt.getTopic().equals(RocketTopicEnums.mobileRechangeTopic)) {
			String channelOrderId = null;
			try {
				channelOrderId = new String(messageExt.getBody(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("body转字符串解析失败");
			}
			//获取分销商订单
			RetailChnlOrderInf retailChnlOrderInf = null;
			ProviderOrderInf telProviderOrderInf = null;

			try {

				logger.info("待发起分销商充值的订单号-->{}", channelOrderId);
				retailChnlOrderInf = retailChnlOrderInfService.getById(channelOrderId);
				if (retailChnlOrderInf != null) {
					telProviderOrderInf = providerOrderInfService.getOrderInfByChannelOrderId(channelOrderId);
				}
			} catch (Exception e) {
				logger.error("## 查询话费充值订单异常-->{}", e);
			}

			if (telProviderOrderInf == null) {
				logger.error("## 未查询到供应商充值订单，渠道订单{}", channelOrderId);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}

			ProviderInf providerInf=null;
			try {
				providerInf = providerInfService.getById(RedisDictKey.zlqf_privoder_code+SpecAccountTypeEnum.B06.getbId());
				if (providerInf == null) {
					logger.error("## 未查询到供应商信息，专项类型供应商ID", RedisDictKey.zlqf_privoder_code+SpecAccountTypeEnum.B06.getbId());
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			} catch (Exception e) {
				logger.error("## 查询渠道信息异常-->{}", e);
			}

			RetailChnlInf retailChnlInf = null;
			try {
				retailChnlInf = retailChnlInfService.getById(retailChnlOrderInf.getChannelId());
			} catch (Exception e) {
				logger.error("## 查询渠道信息异常-->{}", e);
			}

			//10分钟以后的数据订单不出来
			if (System.currentTimeMillis() - retailChnlOrderInf.getCreateTime() < 1000 * 60 * 10) {
				//待充值的订单发起外部充值
				if (telProviderOrderInf != null && TeleConstants.ProviderRechargeState.RECHARGE_STATE_8.getCode().equals((telProviderOrderInf.getRechargeState()))) {
					//商户扣款
					try {
						providerOrderInfService.doMchntCustomerToProvider(providerInf, telProviderOrderInf);
					} catch (Exception e) {
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}

					//请求立方
					PayBillReq payBillReq = new PayBillReq();
					payBillReq.setMobileNo(retailChnlOrderInf.getRechargePhone()); //手机号
					payBillReq.setRechargeAmount(retailChnlOrderInf.getRechargeValue().setScale(0, BigDecimal.ROUND_HALF_DOWN).toString());
					payBillReq.setOuterTid(telProviderOrderInf.getRegOrderId());
					payBillReq.setCallback(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV, "WEB_API_DOMAIN") + "/api/recharge/notify/bm001CallBack");
					logger.info("手机充值--->立方话费充值接口，提交请求链接参数{}", JSONObject.toJSONString(payBillReq));

					try {
						BmRechargeMobilePayBillResponse response = bmOpenApiService.handlePayBill(payBillReq, retailChnlInf.getChannelCode());
						logger.info("BmRechargeMobilePayBillResponse respon -->{}", JSONObject.toJSONString(response));
						OrderDetailInfo orderDetailInfo = null;
						boolean success = false;
						if (response != null) {
							success = response.isSuccess();
							orderDetailInfo = response.getOrderDetailInfo();
						}
						if (!success) {
							//重新反向查询订单状态
							BmOrderCustomGetResponse customOrderResp = bmOpenApiService.handleGetCustomOrder(payBillReq.getOuterTid(), retailChnlInf.getChannelCode());
							logger.info("BmOrderCustomGetResponse customOrderResp -->{}", JSONObject.toJSONString(customOrderResp));
							orderDetailInfo = customOrderResp.getOrderDetailInfo();
						}
						//修改供应商订单信息
						providerOrderInfService.updateOrderRechargeState(telProviderOrderInf, orderDetailInfo, response.getErrorCode());
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					} catch (Exception e) {
						logger.error("##请求话费充值异常-->{}", e);
					}
				}
			} else {
				//取消充值
				try {
					telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_9.getCode());
					providerOrderInfService.updateById(telProviderOrderInf);
				} catch (Exception e) {
					logger.error("##取消话费充值异常-->{}", e);
				}
			}
			//迴調通知分銷商
			retailChnlOrderInfService.doTelRechargeBackNotify(retailChnlInf,retailChnlOrderInf,telProviderOrderInf);
		}
			return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}