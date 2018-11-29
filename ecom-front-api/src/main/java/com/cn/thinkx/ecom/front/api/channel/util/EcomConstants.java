package com.cn.thinkx.ecom.front.api.channel.util;

public class EcomConstants {
	

	/**
	 * ecom channel service 
	 * 
	 * @author xiaomei
	 *
	 */
	public enum EcomChannelService {

		service_h5_ecom("h5.scene.ecom", "自建商城");

		private String code;
		private String name;

		EcomChannelService(String code, String name) {
			this.code = code;
			this.name = name;
		}
		
		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

	}
}
