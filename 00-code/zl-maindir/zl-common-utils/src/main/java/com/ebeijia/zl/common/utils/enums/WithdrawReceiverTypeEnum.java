package com.ebeijia.zl.common.utils.enums;

/**
 * 提现收款方类型
 * @author Administrator
 *
 */
public enum WithdrawReceiverTypeEnum {

	PERSON("PERSON", "个人"),
	CORP("CORP", "企业");

	private String code;
	private String name;

	WithdrawReceiverTypeEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static WithdrawReceiverTypeEnum findByBId(String code) {
		for (WithdrawReceiverTypeEnum t : WithdrawReceiverTypeEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}