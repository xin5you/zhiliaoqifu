package com.ebeijia.zl.web.user.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * 
 * @author zhuqiuyou
 *
 */
@Configuration
public class DubboCustomerConfig {

	@Autowired
	private DubboCustomerProperies dubboCustomerProperies;
	
	/**
	 * dubbo 注册的地址
	 * @return
	 */
	@Bean
	public RegistryConfig registry() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(dubboCustomerProperies.getRegistryAddress());
		return registryConfig;
	}
	
	@Bean
	public ApplicationConfig application(){
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(dubboCustomerProperies.getApplicationName());
		return applicationConfig;
	}
	
//	@Bean
//	public ProtocolConfig protocol(){
//		ProtocolConfig protocolConfig = new ProtocolConfig();
//		
//		protocolConfig.setName(userReferenceProperies.getProtocolName());
//		protocolConfig.setAccepts(userReferenceProperies.getProtocolAccepts());
//		return protocolConfig;
//	}
	
	@Bean
	public ConsumerConfig customer(){
		ConsumerConfig consumerConfig=new ConsumerConfig();
		consumerConfig.setCheck(dubboCustomerProperies.isCheck());
		consumerConfig.setTimeout(1000);
		return consumerConfig;
	}
}
