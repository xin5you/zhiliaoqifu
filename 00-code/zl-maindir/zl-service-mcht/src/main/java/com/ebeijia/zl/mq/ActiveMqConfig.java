package com.ebeijia.zl.mq;


import com.ebeijia.zl.service.telrecharge.listener.BMRechargeMobileSessionAwareMessageListener;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMqConfig {

	@Autowired
	private SingleConnectionFactory connectionFactory;

	@Autowired
	@Qualifier("rechargeMobileMsgSessionAwareQueue")
	private ActiveMQQueue rechargeMobileMsgSessionAwareQueue;

	@Autowired
	private BMRechargeMobileSessionAwareMessageListener rechargeMobileSessionAwareMessageListener;

	@Bean("rechargeMobileSessionAwareMessageListener")
	public	DefaultMessageListenerContainer rechargeMobileSessionAwareMessageListener(){
		DefaultMessageListenerContainer factory = new DefaultMessageListenerContainer();
		factory.setConnectionFactory((ConnectionFactory)connectionFactory);
		factory.setDestination(rechargeMobileMsgSessionAwareQueue);
		factory.setMessageListener(rechargeMobileSessionAwareMessageListener);
		return factory;
	}
}
