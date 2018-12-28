package com.ebeijia.zl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
