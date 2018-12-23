package com.ebeijia.zl.common.utils.enums;

/**
 * 转账标识
 * @author Administrator
 *
 */
public enum TransferCheckEnum {

	INACCOUNT_TRUE("1", "已转账"),
	INACCOUNT_FALSE("0", "未转账");

	private String code;
	private String name;

	TransferCheckEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static TransferCheckEnum findByBId(String code) {
		for (TransferCheckEnum t : TransferCheckEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}