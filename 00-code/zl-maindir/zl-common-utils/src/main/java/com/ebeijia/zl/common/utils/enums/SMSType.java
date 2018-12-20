package com.ebeijia.zl.common.utils.enums;

public enum SMSType {

	SMSType1000("1000","注册验证码"),
	SMSType1001("1001","密码重置"),
	SMSType1002("1002","充值提醒"),
	SMSType1003("1003","卡券转让"),
	SMSType1004("1004","银卡添加");

	private String code;
	private String name;

	SMSType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String findLoginType(String code){
		for (SMSType t : SMSType.values()) {
			if (t.getCode().contains(code)) {
				return t.getName();
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
