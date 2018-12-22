package com.ebeijia.zl.web.api.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboCustomerProperies {

	@Value("${spring.dubbo.application.name}")
	private String applicationName;
	
	@Value("${spring.dubbo.consumer.check}")
	private boolean check;
	
	@Value("${spring.dubbo.registry.address}")
	private String registryAddress;
	
//	@Value("${spring.dubbo.protocol.name}")
//	private String protocolName;
//	
//	@Value("${spring.dubbo.protocol.accepts}")
//	private int protocolAccepts;
	

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

//	public String getProtocolName() {
//		return protocolName;
//	}
//
//	public void setProtocolName(String protocolName) {
//		this.protocolName = protocolName;
//	}
//
//	public int getProtocolAccepts() {
//		return protocolAccepts;
//	}
//
//	public void setProtocolAccepts(int protocolAccepts) {
//		this.protocolAccepts = protocolAccepts;
//	}
//	
	
	
}
