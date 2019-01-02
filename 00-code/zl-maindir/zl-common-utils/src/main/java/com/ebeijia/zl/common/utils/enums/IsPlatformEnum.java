package com.ebeijia.zl.common.utils.enums;

/**
 * 是否为平台账户
 * @author Administrator
 *
 */
public enum IsPlatformEnum {

	IsPlatformEnum_1("1", "是"),
	IsPlatformEnum_0("0", "否");

	private String code;
	private String name;

	IsPlatformEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsPlatformEnum findByBId(String code) {
		for (IsPlatformEnum t : IsPlatformEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}