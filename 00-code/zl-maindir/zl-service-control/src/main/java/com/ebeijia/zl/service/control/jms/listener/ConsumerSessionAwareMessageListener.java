package com.ebeijia.zl.service.control.jms.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.core.activemq.vo.WechatCustomerParam;
import com.ebeijia.zl.core.wechat.process.MpAccount;
import com.ebeijia.zl.core.wechat.process.WxApiClient;




/**
 * 
* 
* @Description: 队列监听器
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月7日 下午5:33:19 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月7日     zhuqi           v1.0.0
 */
public class ConsumerSessionAwareMessageListener implements MessageListener {
	private Logger logger = LoggerFactory.getLogger(ConsumerSessionAwareMessageListener.class);

	@Autowired
	private WxApiClient wxApiClient;
	
	public synchronized void onMessage(Message message) {

		try {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			final String msgStr = msg.getText();
			WechatCustomerParam consumerPatam = com.alibaba.fastjson.JSONObject.parseObject(msgStr, WechatCustomerParam.class);// 转换成相应的对象
			MpAccount mpAccount = wxApiClient.getMpAccount(consumerPatam.getAcountName());
			JSONObject result = wxApiClient.sendCustomTextMessage(consumerPatam.getToOpenId(), consumerPatam.getContent(), mpAccount); //发送客服消息
			
			if (result != null && result.getIntValue("errcode") == 0) {
				message.acknowledge();
			}
		} catch (Exception e) {
			logger.error("## 发送的客服消息异常：{}", e);
		}
	}
}