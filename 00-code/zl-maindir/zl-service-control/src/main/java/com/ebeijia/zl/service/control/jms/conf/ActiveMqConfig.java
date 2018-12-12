package com.ebeijia.zl.service.control.jms.conf;

import javax.jms.ConnectionFactory;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.ebeijia.zl.service.control.jms.listener.ConsumerSessionAwareMessageListener;

@Configuration
public class ActiveMqConfig {

	@Autowired
	private SingleConnectionFactory connectionFactory;
	
//	@Autowired
//	private ConsumerSessionAwareMessageListener consumerSessionAwareMessageListener;
	
	@Autowired
	private ActiveMQQueue consumerMsgSessionAwareQueue;
	
	@Bean("consumerSessionAwareMessageListener")
	public	DefaultMessageListenerContainer consumerSessionAwareMessageListener(){
		DefaultMessageListenerContainer factory = new DefaultMessageListenerContainer();
		 factory.setConnectionFactory((ConnectionFactory)connectionFactory);
		 factory.setDestination(consumerMsgSessionAwareQueue);
		 factory.setMessageListener(new ConsumerSessionAwareMessageListener());
	    return factory;
	}
}
