package com.ebeijia.zl.common.utils.enums;

/**
 * 收款标识
 * @author Administrator
 *
 */
public enum ReceiverEnum {

	RECEIVER_TRUE("1", "已收款"),
	RECEIVER_FALSE("0", "未收款");

	private String code;
	private String name;

	ReceiverEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static ReceiverEnum findByBId(String code) {
		for (ReceiverEnum t : ReceiverEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}