package com.ebeijia.zl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ftpClient")
public class FtpProps {

	 private Map<String, String> connect = new HashMap<>();
	
	 public Map<String, String> getConnect() {
	 	return connect;
	 }
	
	 public void setConnect(Map<String, String> connect) {
	 this.connect = connect;
	 }

}
