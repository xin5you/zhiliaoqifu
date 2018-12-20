package com.ebeijia.zl.common.utils.enums;

/**
 * 打款标识
 * @author Administrator
 *
 */
public enum RemitCheckEnum {

	REMIT_TRUE("1", "已打款"),
	REMIT_FALSE("0", "未打款");

	private String code;
	private String name;

	RemitCheckEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static RemitCheckEnum findByBId(String code) {
		for (RemitCheckEnum t : RemitCheckEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}