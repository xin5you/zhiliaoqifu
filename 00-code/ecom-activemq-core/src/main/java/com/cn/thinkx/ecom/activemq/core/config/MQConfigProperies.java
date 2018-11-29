package com.cn.thinkx.ecom.activemq.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfigProperies {

	@Value("${spring.mq.brokerURL}")
	private String brokerURL; // mq 集群所有节点

	@Value("${spring.mq.username}")
	private String username;

	@Value("${spring.mq.password}")
	private String password;

	@Value("${spring.mq.pool.max-connections}")
	private int maxConnections;

	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

}
