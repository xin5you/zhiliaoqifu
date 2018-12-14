package com.ebeijia.zl.common.utils.enums;

/**
 * 开户状态
 * @author Administrator
 *
 */
public enum IsOpenEnum{

	ISOPEN_TRUE("1", "已开户"),
	ISOPEN_FALSE("0", "未开户");

	private String code;
	private String name;

	IsOpenEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsOpenEnum findByBId(String code) {
		for (IsOpenEnum t : IsOpenEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}