package com.cn.thinkx.ecom;

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

//	private Connect connect;
//
//	public Connect getConnect() {
//		return connect;
//	}
//
//	public void setConnect(Connect connect) {
//		this.connect = connect;
//	}
//
//	public static class Connect {
//		private String server;
//		private String port;
//		private String username;
//		private String password;
//		private String separator;
//
//		public String getServer() {
//			return server;
//		}
//
//		public void setServer(String server) {
//			this.server = server;
//		}
//
//		public String getPort() {
//			return port;
//		}
//
//		public void setPort(String port) {
//			this.port = port;
//		}
//
//		public String getUsername() {
//			return username;
//		}
//
//		public void setUsername(String username) {
//			this.username = username;
//		}
//
//		public String getPassword() {
//			return password;
//		}
//
//		public void setPassword(String password) {
//			this.password = password;
//		}
//
//		public String getSeparator() {
//			return separator;
//		}
//
//		public void setSeparator(String separator) {
//			this.separator = separator;
//		}
//
//	}

}
