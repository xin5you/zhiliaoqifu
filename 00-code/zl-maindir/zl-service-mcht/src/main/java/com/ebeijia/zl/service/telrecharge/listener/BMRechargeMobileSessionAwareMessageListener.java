package com.ebeijia.zl.service.telrecharge.listener;

import java.math.BigDecimal;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.ebeijia.zl.api.bm001.api.req.PayBillReq;
import com.ebeijia.zl.api.bm001.api.service.BMOpenApiService;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;
import com.qianmi.open.api.response.BmOrderCustomGetResponse;
import com.qianmi.open.api.response.BmRechargeMobilePayBillResponse;
import org.apache.activemq.command.ActiveMQTextMessage;
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

/**
 * 立方话费充值
 * 
 * @author zhuqiuyou
 * 
 * @since 2018-07-10 11:21:23
 *  
 */
@Configuration
public class BMRechargeMobileSessionAwareMessageListener implements MessageListener {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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

	public synchronized void onMessage(Message message) {
		ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			
			//获取分销商订单
			RetailChnlOrderInf retailChnlOrderInf=null;
			ProviderOrderInf telProviderOrderInf=null;
			try {
				 String channelOrderId = msg.getText();
				 logger.info("待发起分销商充值的订单号-->{}", channelOrderId);
				 retailChnlOrderInf=retailChnlOrderInfService.getById(channelOrderId);
				 if(retailChnlOrderInf !=null){
					 telProviderOrderInf=providerOrderInfService.getOrderInfByChannelOrderId(channelOrderId);
				  }
			} catch (Exception e) {
				logger.error("## 查询话费充值订单异常-->{}", e);
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
					payBillReq.setRechargeAmount(retailChnlOrderInf.getRechargeValue().toString());
					payBillReq.setOuterTid(telProviderOrderInf.getRegOrderId());
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
						if(orderDetailInfo==null){
							orderDetailInfo=new OrderDetailInfo();
						}
						//订单充值状态 0充值中 1成功 9撤销
						String rechargeState= StringUtils.isNotEmpty(orderDetailInfo.getRechargeState())? orderDetailInfo.getRechargeState():"";
						switch (rechargeState){
							case "0":
								telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
								break;
							case  "1":
								telProviderOrderInf.setItemCost(new BigDecimal(orderDetailInfo.getItemCost()));  //商品成本价(进价)，单位元，保留3位小数
								telProviderOrderInf.setTransCost(new BigDecimal(orderDetailInfo.getOrderCost())); //订单成本(进价)，单位元，保留3位小数，orderCost=itemCost*itemNum
								telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_1.getCode());
								break;
							default:
								telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode());
								break;
						}
						telProviderOrderInf.setBillId(orderDetailInfo.getBillId());

						//订单付款状态 0 未付款1 已付款
						telProviderOrderInf.setPayState(StringUtils.isEmpty(orderDetailInfo.getPayState()) ? "0": orderDetailInfo.getPayState());
				        telProviderOrderInf.setResv1(response.getErrorCode()); //记录充值渠道返回的结果信息

						telProviderOrderInf.setOperateTime(System.currentTimeMillis());
				        providerOrderInfService.updateById(telProviderOrderInf);
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
		if( "0".equals(retailChnlOrderInf.getNotifyFlag())){
			try{
				//异步通知供应商
				TeleRespVO respVo=new TeleRespVO();
				respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
				respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
				respVo.setPayState(retailChnlOrderInf.getOrderStat());
				respVo.setRechargeState(telProviderOrderInf.getRechargeState()); //充值状态
				if(telProviderOrderInf.getOperateTime() !=null){
					respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(new Date(telProviderOrderInf.getOperateTime())));
				}
				respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(retailChnlOrderInf.getCreateTime()))); //操作时间
				respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
				respVo.setItemNum(retailChnlOrderInf.getItemNum());
				respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
				respVo.setChannelId(retailChnlOrderInf.getChannelId());
				respVo.setChannelToken(retailChnlInf.getChannelCode());
				respVo.setV(retailChnlOrderInf.getAppVersion());
				respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
				respVo.setSubErrorCode(telProviderOrderInf.getResv1());
				if(ReqMethodCode.R1.getCode().equals(retailChnlOrderInf.getRechargeType())){
					respVo.setMethod(ReqMethodCode.R1.getValue());
				}else if(ReqMethodCode.R2.getCode().equals(retailChnlOrderInf.getRechargeType())){
					respVo.setMethod(ReqMethodCode.R2.getValue());
				}
				String psotToken=MD5SignUtils.genSign(respVo, "key",retailChnlInf.getChannelKey(), new String[]{"sign","serialVersionUID"}, null);
				respVo.setSign(psotToken);
				
				//修改通知后 分销商的处理状态
				logger.info("##发起分销商回调[{}],返回参数:[{}]",retailChnlOrderInf.getNotifyUrl(),JSONObject.toJSONString(ResultsUtil.success(respVo)));
				String result=HttpClientUtil.sendPostReturnStr(retailChnlOrderInf.getNotifyUrl(),JSONObject.toJSONString(ResultsUtil.success(respVo)));
				if(result !=null && "SUCCESS ".equals(result.toUpperCase() )){
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
				}else{
					retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
				}
				retailChnlOrderInfService.updateById(retailChnlOrderInf);
				} catch (Exception e) {
					logger.error("##话费充值失败，回调分销商异常-->{}", e);
				}
			}
			try {
				message.acknowledge();
			} catch (JMSException e) {
				logger.error("##消息ack确认发生异常-->{}", e);
			}
		}
}