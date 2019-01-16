package com.ebeijia.zl.service.telrecharge.listener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


import com.ebeijia.zl.api.bm001.api.req.PayBillReq;
import com.ebeijia.zl.api.bm001.api.service.BMOpenApiService;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.service.telrecharge.service.ProviderOrderInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlInfService;
import com.ebeijia.zl.service.telrecharge.service.RetailChnlOrderInfService;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisCluster;

/**
 * 立方话费充值
 * 
 * @author zhuqiuyou
 * 
 * @since 2018-07-10 11:21:23
 *  
 */
@MQConsumer(topic = RocketTopicEnums.mobileRechangeTopic, consumerGroup = "zlqf_group")
public class BMRechargeMobileSessionAwareMessageListener extends AbstractMQPushConsumer {
	
	private Logger logger = LoggerFactory.getLogger(BMRechargeMobileSessionAwareMessageListener.class);
	
	@Autowired
	private ProviderOrderInfService providerOrderInfService;

	@Autowired
	private RetailChnlOrderInfService retailChnlOrderInfService;
	
	@Autowired
	private RetailChnlInfService retailChnlInfService;
	
	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Autowired
	private BMOpenApiService bmOpenApiService;

	@Autowired
	private JedisCluster  jedisCluster;

	@Override
	public boolean process(Object message, Map map) {

		logger.info("待发送的消息message={}", message);
		logger.info("待发送的消息map=>{}", JSONObject.toJSONString(map));
			//获取分销商订单
			RetailChnlOrderInf retailChnlOrderInf=null;
			ProviderOrderInf telProviderOrderInf=null;
			try {
				 String channelOrderId = String.valueOf(message);
				 logger.info("待发起分销商充值的订单号-->{}", channelOrderId);
				 retailChnlOrderInf=retailChnlOrderInfService.getById(channelOrderId);
				 if(retailChnlOrderInf !=null){
					 telProviderOrderInf=providerOrderInfService.getOrderInfByChannelOrderId(channelOrderId);
				  }
			} catch (Exception e) {
				logger.error("## 查询话费充值订单异常-->{}", e);
			}

			if(telProviderOrderInf==null){
				logger.error("## 未查询到供应商充值订单，渠道订单{}", message);
				return true;
			}
			
			RetailChnlInf retailChnlInf=null;
			try{
				retailChnlInf= retailChnlInfService.getById(retailChnlOrderInf.getChannelId());
			} catch (Exception e) {
				logger.error("## 查询渠道信息异常-->{}", e);
			}

			//10分钟以后的数据订单不出来
//			Date currDate=new Date();
			if(System.currentTimeMillis()-retailChnlOrderInf.getCreateTime()<1000*60*10){
				//待充值的订单发起外部充值
				if(telProviderOrderInf !=null && TeleConstants.ProviderRechargeState.RECHARGE_STATE_8.getCode().equals( (telProviderOrderInf.getRechargeState()))){

					PayBillReq payBillReq=new PayBillReq();
					payBillReq.setMobileNo(retailChnlOrderInf.getRechargePhone()); //手机号
					payBillReq.setRechargeAmount(retailChnlOrderInf.getRechargeValue().setScale(0,BigDecimal.ROUND_HALF_DOWN).toString());
					payBillReq.setOuterTid(telProviderOrderInf.getRegOrderId());
					payBillReq.setCallback(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WEB_API_DOMAIN")+"/api/recharge/notify/bm001CallBack");
					logger.info("手机充值--->立方话费充值接口，提交请求链接参数{}", JSONObject.toJSONString(payBillReq));

					try {
						BmRechargeMobilePayBillResponse response=bmOpenApiService.handlePayBill(payBillReq,retailChnlInf.getChannelCode());
						logger.info("BmRechargeMobilePayBillResponse respon -->{}",JSONObject.toJSONString(response));
						OrderDetailInfo orderDetailInfo=null;
						boolean success=false;
						if (response != null) {
							success=response.isSuccess();
							orderDetailInfo=response.getOrderDetailInfo();
						}
						if(!success){
							//重新反向查询订单状态
							BmOrderCustomGetResponse customOrderResp = bmOpenApiService.handleGetCustomOrder(payBillReq.getOuterTid(), retailChnlInf.getChannelCode());
							logger.info("BmOrderCustomGetResponse customOrderResp -->{}",JSONObject.toJSONString(customOrderResp));
							orderDetailInfo=customOrderResp.getOrderDetailInfo();
						}
						//修改供应商订单信息
						providerOrderInfService.updateOrderRechargeState(telProviderOrderInf,orderDetailInfo,response.getErrorCode());

						return true;
					} catch (Exception e) {
						logger.error("##请求话费充值异常-->{}", e);
					}
				}
			}else{
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

		return true;
	}
}