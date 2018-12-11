package com.cn.thinkx.wecard.facade.telrecharge.listener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.wecard.facade.telrecharge.domain.ProviderOrderInf;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlInf;
import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlOrderInf;
import com.cn.thinkx.wecard.facade.telrecharge.resp.TeleRespVO;
import com.cn.thinkx.wecard.facade.telrecharge.service.ProviderOrderInfService;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlInfFacade;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlInfService;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.cn.thinkx.wecard.facade.telrecharge.service.RetailChnlOrderInfService;
import com.cn.thinkx.wecard.facade.telrecharge.utils.ResultsUtil;
import com.cn.thinkx.wecard.facade.telrecharge.utils.TeleConstants;
import com.cn.thinkx.wecard.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.cn.thinkx.wecard.facade.telrecharge.vo.FrtPhoneRechargeReq;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.MD5SignUtils;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 模板消息队列监听器
 * 
 * @author zhuqiuyou
 * 
 * @since 2018-07-10 11:21:23
 *  
 */
public class RechargeMobileSessionAwareMessageListener implements MessageListener {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("providerOrderInfService")
	private ProviderOrderInfService providerOrderInfService;
	
	@Autowired
	@Qualifier("retailChnlOrderInfService")
	private RetailChnlOrderInfService retailChnlOrderInfService;
	
	@Autowired
	@Qualifier("retailChnlInfService")
	private RetailChnlInfService retailChnlInfService;
	
	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

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
					 telProviderOrderInf=providerOrderInfService.getTelOrderInfByChannelOrderId(channelOrderId);
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
			
			boolean notifyFlag=false; //是否回调分销商
			//30分钟以后的数据订单不出来
//			Date currDate=new Date();
			if(System.currentTimeMillis()-retailChnlOrderInf.getCreateTime()<1000*60*30){
				//待充值的订单发起外部充值
				if(telProviderOrderInf !=null && TeleConstants.ProviderRechargeState.RECHARGE_STATE_8.getCode().equals( (telProviderOrderInf.getRechargeState()))){
					FrtPhoneRechargeReq frtReq=new FrtPhoneRechargeReq();
					frtReq.setAccessToken(retailChnlInf.getChannelCode());
					frtReq.setChannelOrderNo(telProviderOrderInf.getRegOrderId());
					frtReq.setPhone(retailChnlOrderInf.getRechargePhone());
					frtReq.setTelephoneFace(retailChnlOrderInf.getRechargeValue().setScale(0, BigDecimal.ROUND_DOWN).toString()); //面额
					frtReq.setOrderType("1");
					frtReq.setReqChannel("P1003");
					frtReq.setAttach(jedisClusterUtils.hget("TB_BASE_DICT_KV", "P1003_SIGN_KEY"));
					frtReq.setTimestamp(System.currentTimeMillis());
					frtReq.setCallBack(jedisClusterUtils.hget("TB_BASE_DICT_KV", "WECARD_API_DOMAIN_URL")+"api/recharge/notify/bmHKbCallBack.html");
					String sign=MD5SignUtils.genSign(frtReq, 
							"key",
							jedisClusterUtils.hget("TB_BASE_DICT_KV", "P1003_SIGN_KEY"), 
							 new String[]{"sign","serialVersionUID"}, 
							 null);
					frtReq.setSign(sign);
				
					
					//获取所有的数据的fileds & vlaue
					Map<String, String> parameters=MD5SignUtils.getObjectMaps(frtReq, new String[]{"serialVersionUID"}, null);
					String rechargeUrl=jedisClusterUtils.hget("TB_BASE_DICT_KV", "WELFAREMART_RECHARGE_REQUEST_URL");
					String reqString=JSONObject.toJSONString(frtReq);
					logger.info("手机充值--->流量充值接口，提交请求链接[{}] 参数{}", rechargeUrl, reqString);

					try {
						String jsonString= HttpClientUtil.sendPost(rechargeUrl, reqString);
						logger.info("##话费充值返回数据-->{}",jsonString);
						
						ObjectMapper objectMapper = new ObjectMapper();  
				        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号  
				        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括  
				        JsonNode root = objectMapper.readTree(jsonString); 
				       String  code= root.get("code").asText();
				       if("00".equals(code)){
				    	   String billId=root.get("orderId").asText();
				    	   //已经发起充值
				    	   telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_0.getCode());
				    	   telProviderOrderInf.setBillId(billId);
				       }else{
				    	   notifyFlag=true; //回调分销商
				    	   telProviderOrderInf.setRechargeState(TeleConstants.ProviderRechargeState.RECHARGE_STATE_3.getCode());
				       }
				       telProviderOrderInf.setResv1(code); //记录充值渠道返回的结果信息
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
				 notifyFlag=true; //回调分销商
				} catch (Exception e) {
					logger.error("##取消话费充值异常-->{}", e);
				}
		 }
			
		if(notifyFlag &&  "0".equals(retailChnlOrderInf.getNotifyFlag())){
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
				if("1".equals(retailChnlOrderInf.getRechargeType())){
					respVo.setMethod(ReqMethodCode.R1.getValue());
				}else if("2".equals(retailChnlOrderInf.getRechargeType())){
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