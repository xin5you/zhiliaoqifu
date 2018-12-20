package com.ebeijia.zl.common.utils.enums;

/**
 * 上账标识
 * @author Administrator
 *
 */
public enum InaccountCheckEnum {

	INACCOUNT_TRUE("1", "已上账"),
	INACCOUNT_FALSE("0", "未上账");

	private String code;
	private String name;

	InaccountCheckEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static InaccountCheckEnum findByBId(String code) {
		for (InaccountCheckEnum t : InaccountCheckEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}