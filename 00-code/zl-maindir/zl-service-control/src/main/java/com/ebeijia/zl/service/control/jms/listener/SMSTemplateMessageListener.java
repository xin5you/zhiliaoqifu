package com.ebeijia.zl.service.control.jms.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.SmsVo;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.rocketmq.enums.RocketTopicEnums;

import com.ebeijia.zl.service.control.jms.enums.AliyunSMSTemplateCode;
import com.ebeijia.zl.service.control.sms.domain.TbSmsDetails;
import com.ebeijia.zl.service.control.sms.service.ITbSmsDetailsService;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisCluster;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 模板消息队列监听器
 *
 * @author 朱秋友
 *
 * @since 2018-12-27 11:21:23
 *
 */
@Component
public class SMSTemplateMessageListener implements MessageListenerConcurrently {
	private Logger logger = LoggerFactory.getLogger(SMSTemplateMessageListener.class);

	@Autowired
	private JedisCluster jedisCluster;

	@Autowired
	private IAcsClient acsClient;

	@Autowired
	private ITbSmsDetailsService iTbSmsDetailsService;

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		if(CollectionUtils.isEmpty(msgs)){
			//接受到的消息为空，不处理，直接返回成功
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		MessageExt messageExt = msgs.get(0);
		logger.info("接受到的消息为："+messageExt.toString());

		if(messageExt.getTopic().equals(RocketTopicEnums.smsTopic)){
			String mess="";
			try {
				mess = new String(messageExt.getBody(), "UTF-8");
			} catch (UnsupportedEncodingException e){
				logger.error("body转字符串解析失败");
			}
			SmsVo sms = JSONObject.parseObject(mess, SmsVo.class);
			TbSmsDetails tbSmsDetails = new TbSmsDetails();
			tbSmsDetails.setMsgId(StringUtil.isNotEmpty(sms.getMsgId())? sms.getMsgId():IdUtil.getNextId());
			tbSmsDetails.setPhoneNumber(sms.getPhoneNumber());
			tbSmsDetails.setTemplateParam(JSONArray.toJSONString(sms));
			tbSmsDetails.setSmsType(sms.getSmsType());
			tbSmsDetails.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			tbSmsDetails.setCreateTime(System.currentTimeMillis());
			tbSmsDetails.setCreateUser("99999999");
			tbSmsDetails.setUpdateTime(System.currentTimeMillis());
			tbSmsDetails.setUpdateUser("99999999");
			tbSmsDetails.setLockVersion(0);
			iTbSmsDetailsService.save(tbSmsDetails);

			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//组装请求对象-具体描述见控制台-文档部分内容
			SendSmsRequest request = new SendSmsRequest();
			//必填:待发送手机号
			request.setPhoneNumbers(sms.getPhoneNumber());
			//必填:短信签名-可在短信控制台中找到
			request.setSignName("知了企服");

			//必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(AliyunSMSTemplateCode.findByCode(sms.getSmsType()).getAliCode());

			//可选:模板中的变量替换JSON串,如模板内容为"验证码：${code}（有效期5分钟）您正在操作<注册>业务，切勿告知他人！！
			request.setTemplateParam("{\"code\":\""+sms.getCode()+"\"}");

			//hint 此处可能会抛出异常，注意catch
			try {
				SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

				tbSmsDetails.setRespId(sendSmsResponse.getRequestId());
				tbSmsDetails.setRespCode(sendSmsResponse.getCode());
				tbSmsDetails.setRespMsg(sendSmsResponse.getMessage());
				tbSmsDetails.setUpdateTime(System.currentTimeMillis());
				iTbSmsDetailsService.updateById(tbSmsDetails);
				if("OK".equals(sendSmsResponse.getCode())){
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}else{
					logger.error("## 短信发送失败：[{}]",JSONObject.toJSONString(sendSmsResponse));
				}
			}catch (Exception ex){
				logger.error("发送短信异常：{}",ex);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		// 如果没有return success ，consumer会重新消费该消息，直到return success
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}


}
