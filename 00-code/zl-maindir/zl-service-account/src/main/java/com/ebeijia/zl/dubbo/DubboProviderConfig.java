package com.ebeijia.zl.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * 
 * @author zhuqiuyou
 *
 */
@Configuration
public class DubboProviderConfig {

	@Autowired
	private DubboProviderProperies dubboProviderProperies;
	
	/**
	 * dubbo 注册的地址
	 * @return
	 */
	@Bean
	public RegistryConfig registry() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(dubboProviderProperies.getRegistryAddress());
		return registryConfig;
	}
	
	@Bean
	public ApplicationConfig application(){
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(dubboProviderProperies.getApplicationName());
		return applicationConfig;
	}
	
	@Bean
	public ProtocolConfig protocol(){
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName(dubboProviderProperies.getProtocolName());
//		protocolConfig.setHost(mchntProvideProperies.getProtocolHost());
		protocolConfig.setPort(dubboProviderProperies.getProtocolPort());
		protocolConfig.setAccepts(dubboProviderProperies.getProtocolAccepts());
		return protocolConfig;
	}
	
	
	@Bean
	public ProviderConfig provider(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig){
		ProviderConfig providerConfig=new ProviderConfig();
	
		providerConfig.setId(dubboProviderProperies.getProviderId());
		providerConfig.setTimeout(dubboProviderProperies.getProviderTimeout());
		providerConfig.setDelay(dubboProviderProperies.getProviderDelay());
		providerConfig.setApplication(applicationConfig);
		providerConfig.setRegistry(registryConfig);
		providerConfig.setProtocol(protocolConfig);
		return providerConfig;
	}
}
