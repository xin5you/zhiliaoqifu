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
import com.ebeijia.zl.core.activemq.vo.WechatTemplateParam;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.wechat.process.MpAccount;
import com.ebeijia.zl.core.wechat.process.WxApiClient;
import com.ebeijia.zl.core.wechat.vo.TemplateMessage;
import com.ebeijia.zl.service.control.jms.enums.AliyunSMSTemplateCode;
import com.ebeijia.zl.service.control.sms.domain.TbSmsDetails;
import com.ebeijia.zl.service.control.sms.service.ITbSmsDetailsService;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisCluster;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 模板消息队列监听器
 * 
 * @author 朱秋友
 * 
 * @since 2017-01-17 11:21:23
 *  
 */
@Configuration
public class SMSTemplateMessageListener implements MessageListener {
	private Logger logger = LoggerFactory.getLogger(SMSTemplateMessageListener.class);

	@Autowired
	private JedisCluster jedisCluster;

	@Autowired
	private IAcsClient acsClient;

	@Autowired
	private ITbSmsDetailsService smsDetailsService;

	public synchronized void onMessage(Message message) {

		try {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			final String ms = msg.getText();
			logger.info("待发送的短信消息{}", ms);

			SmsVo sms = JSONObject.parseObject(ms, SmsVo.class);


            TbSmsDetails tbSmsDetails = new TbSmsDetails();
            tbSmsDetails.setMsgId(StringUtil.isNotEmpty(sms.getMsgId())? sms.getMsgId():IdUtil.getNextId());
            tbSmsDetails.setPhoneNumber(sms.getPhoneNumber());
            tbSmsDetails.setTemplateParam(JSONArray.toJSONString(sms));
            tbSmsDetails.setSmsType(sms.getSmsType());
            tbSmsDetails.setData(DataStatEnum.TRUE_STATUS.getCode());
            tbSmsDetails.setCreateTime(System.currentTimeMillis());
            tbSmsDetails.setCreateUser("99999999");
            tbSmsDetails.setUpdateTime(System.currentTimeMillis());
            tbSmsDetails.setUpdateUser("99999999");
            tbSmsDetails.setLockVersion(0);

            smsDetailsService.save(tbSmsDetails);

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
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            tbSmsDetails.setRespId(sendSmsResponse.getRequestId());
            tbSmsDetails.setRespCode(sendSmsResponse.getCode());
            tbSmsDetails.setRespMsg(sendSmsResponse.getMessage());
            tbSmsDetails.setUpdateTime(System.currentTimeMillis());
            smsDetailsService.updateById(tbSmsDetails);
			if("OK".equals(sendSmsResponse.getCode())){
				message.acknowledge();
			}else{
			    logger.error("## 短信发送失败：[{}]",JSONObject.toJSONString(sendSmsResponse));
            }
		} catch (Exception e) {
			logger.error("## 待发送的短信消息异常：", e);
		}

	}
}