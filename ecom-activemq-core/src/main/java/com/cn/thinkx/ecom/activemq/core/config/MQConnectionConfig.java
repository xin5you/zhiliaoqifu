package com.cn.thinkx.ecom.activemq.core.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.SingleConnectionFactory;



/**
 * activemq config
 * 
 * @author zhuqiuyou
 *
 */
@Configuration
public class MQConnectionConfig {
	
	
	@Autowired
	private MQConfigProperies mqConfigProperies;
	
	/**
	 * 
	 * 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供
	 * @return
	 */
	@Bean(name="targetConnectionFactory")
	public ActiveMQConnectionFactory targetConnectionFactory(){
		ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory(mqConfigProperies.getUsername(),mqConfigProperies.getPassword(),mqConfigProperies.getBrokerURL());
		return connectionFactory;
	}
	
	/**
	 *	ActiveMQ为我们提供了一个PooledConnectionFactory，通过往里面注入一个ActiveMQConnectionFactory
	 *	可以用来将Connection、Session和MessageProducer池化，这样可以大大的减少我们的资源消耗。
	 *	要依赖于 activemq-pool包
	 *	@return
	 */
	@Bean("pooledConnectionFactory")
	public PooledConnectionFactory pooledConnectionFactory(){
		PooledConnectionFactory poolConnetion=new PooledConnectionFactory();
		poolConnetion.setConnectionFactory(targetConnectionFactory());
		poolConnetion.setMaxConnections(mqConfigProperies.getMaxConnections());
		return poolConnetion;
	}
	
	/**
	 * Spring用于管理真正的ConnectionFactory的ConnectionFactory
	 * @return
	 */
	@Bean("activemqConnectionFactory")
	@Primary
	public SingleConnectionFactory connectionFactory(){
		SingleConnectionFactory ConnectionFactory=new SingleConnectionFactory(pooledConnectionFactory());
		return ConnectionFactory;
	}

}
