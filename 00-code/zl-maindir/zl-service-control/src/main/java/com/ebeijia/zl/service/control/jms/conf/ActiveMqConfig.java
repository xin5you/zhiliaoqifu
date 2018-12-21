package com.ebeijia.zl.service.control.jms.conf;

import javax.jms.ConnectionFactory;

import com.ebeijia.zl.service.control.jms.listener.SMSTemplateMessageListener;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.ebeijia.zl.service.control.jms.listener.ConsumerSessionAwareMessageListener;

@Configuration
public class ActiveMqConfig {

	@Autowired
	private SingleConnectionFactory connectionFactory;

	@Autowired
	@Qualifier("consumerMsgSessionAwareQueue")
	private ActiveMQQueue consumerMsgSessionAwareQueue;

	@Autowired
	@Qualifier("smsMsgSessionAwareQueue")
	private ActiveMQQueue smsMsgSessionAwareQueue;

	@Autowired
	private SMSTemplateMessageListener smsTemplateMessageListener;
	
	@Bean("consumerSessionAwareMessageListener")
	public	DefaultMessageListenerContainer consumerSessionAwareMessageListener(){
		DefaultMessageListenerContainer factory = new DefaultMessageListenerContainer();
		 factory.setConnectionFactory((ConnectionFactory)connectionFactory);
		 factory.setDestination(consumerMsgSessionAwareQueue);
		 factory.setMessageListener(new ConsumerSessionAwareMessageListener());
	    return factory;
	}


	@Bean("smsSessionAwareMessageListener")
	public	DefaultMessageListenerContainer smsSessionAwareMessageListener(){
		DefaultMessageListenerContainer factory = new DefaultMessageListenerContainer();
		factory.setConnectionFactory((ConnectionFactory)connectionFactory);
		factory.setDestination(smsMsgSessionAwareQueue);
		factory.setMessageListener(smsTemplateMessageListener);
		return factory;
	}
}
