package com.ebeijia.zl.common.utils.enums;

public enum LoginType {

	LoginType1("1","OMS(运营管理平台)"),
	LoginType2("2","CMS(电商管理平台)"),
	LoginType3("3","DIY(商户自助服务平台)");

	private String code;
	private String name;

	LoginType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String findLoginType(String code){
		for (LoginType t : LoginType.values()) {
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
