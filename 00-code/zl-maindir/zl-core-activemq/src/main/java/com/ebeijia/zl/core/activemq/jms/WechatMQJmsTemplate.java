package com.ebeijia.zl.core.activemq.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class WechatMQJmsTemplate {
	
	
	private final String consumerMsgSessionQueue="wecard.activemq.send.wechat.msg.v1";
	
	@Autowired
	@Qualifier("activemqConnectionFactory")
	private SingleConnectionFactory connectionFactory;
	
	
	
	@Bean
	public ActiveMQQueue consumerMsgSessionAwareQueue(){
		ActiveMQQueue queue=new ActiveMQQueue(consumerMsgSessionQueue);
		return queue;
		
	}
	
	/**
	 * 
	* @Description: 微信开户消息
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月7日 下午3:23:15 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月7日     zhuqi           v1.0.0
	 */
	@Bean(name="consumerMsgJmsTemplate")
	public JmsTemplate getConsumerMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(consumerMsgSessionQueue);
		return jmsTemplate;
	}
	
	@Bean(name="templateMsgJmsTemplate")
	public JmsTemplate getTemplateMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("wecard.wechat.mcht.collection.bill.template.msg.v1");
		return jmsTemplate;
	}
	
	@Bean(name="rechargeMobileJmsTemplate")
	public JmsTemplate getRechargeMobileJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("hkb.api.jms.template.recharge.mobile.v1");
		return jmsTemplate;
	}

}
