package com.ebeijia.zl.common.utils.enums;
	/**
	 *
	 * 用户所属渠道类型
	 *
	 */
	public enum UserChnlCode {
		USERCHNL1001("1001","管理平台"),
		USERCHNL1002("1002","知了商城"),
		USERCHNL2001("2001","微信公众号"),
		USERCHNL3001("3001","微信小程序"),
		USERCHNL4001("4001","支付宝用户");

		private  String code;
		private  String value;
		

		public String getCode() {
			return code;
		}


		public String getValue() {
			return value;
		}

		UserChnlCode(String code,String value) {
			this.code=code;
			this.value = value;
		};
	
	}