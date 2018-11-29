package com.cn.thinkx.ecom.activemq.core.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class WechatMQJmsTemplate {
	
	@Autowired
	@Qualifier("activemqConnectionFactory")
	private SingleConnectionFactory connectionFactory;
	
	
	@Bean(name="consumerMsgJmsTemplate")
	public JmsTemplate getConsumerMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("wecard.activemq.send.wechat.msg.v1");
		return jmsTemplate;
	}
	
	@Bean(name="templateMsgJmsTemplate")
	public JmsTemplate getTemplateMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("wecard.wechat.mcht.collection.bill.template.msg.v1");
		return jmsTemplate;
	}

}
