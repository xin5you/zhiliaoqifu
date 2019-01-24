package com.ebeijia.zl.common.utils.enums;
/**
 *
 * 賬戶类型
 * 100：企业员工账户
 * 200：企业账户
 * 300：供应商账户
 * 400：分销商账户
 */
public enum UserType {
	/**

	 */
	TYPE100("100", "企业员工账户"), 
	TYPE200("200", "企业账户"),
	TYPE300("300", "供应商账户"),
	TYPE400("400", "分销商账户"),
	TYPE500("500", "平台账户");
	
	private String code;

	private String value;

	UserType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static UserType findByCode(String code) {
		for (UserType t : UserType.values()) {
			if (t.getCode().equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}
