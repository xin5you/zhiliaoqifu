package com.ebeijia.zl.core.activemq.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MQJmsTemplate {
	
	
	private final String consumerMsgSessionQueue="zlqf.activemq.send.wechat.msg.v1";
	private final String smsMsgSessionQueue="zlqf.activemq.send.sms.msg.v1";

	@Autowired
	@Qualifier("activemqConnectionFactory")
	private SingleConnectionFactory connectionFactory;
	
	
	
	@Bean("consumerMsgSessionAwareQueue")
	public ActiveMQQueue consumerMsgSessionAwareQueue(){
		ActiveMQQueue queue=new ActiveMQQueue(consumerMsgSessionQueue);
		return queue;
	}

	@Bean("smsMsgSessionAwareQueue")
	public ActiveMQQueue smsMsgSessionAwareQueue(){
		ActiveMQQueue queue=new ActiveMQQueue(smsMsgSessionQueue);
		return queue;
	}
	

	@Bean(name="consumerMsgJmsTemplate")
	public JmsTemplate consumerMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(consumerMsgSessionQueue);
		return jmsTemplate;
	}

	@Bean(name="smsMsgJmsTemplate")
	public JmsTemplate smsMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(smsMsgSessionQueue);
		return jmsTemplate;
	}
	
	@Bean(name="templateMsgJmsTemplate")
	public JmsTemplate getTemplateMsgJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("zlqf.activemq.send.template.msg.v1");
		return jmsTemplate;
	}
	
	@Bean(name="rechargeMobileJmsTemplate")
	public JmsTemplate getRechargeMobileJmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName("zlqf.activemq.send.recharge.mobile.v1");
		return jmsTemplate;
	}

}
