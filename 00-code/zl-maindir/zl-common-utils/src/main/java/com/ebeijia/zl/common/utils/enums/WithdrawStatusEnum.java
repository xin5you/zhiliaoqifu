package com.ebeijia.zl.common.utils.enums;

/**
 * 提现订单状态
 * @author Administrator
 *
 */
public enum WithdrawStatusEnum {

	TRUE("true", "成功"),
	FALSE("false", "失败"),
	PROCESSING("processing", "处理中");

	private String code;
	private String name;

	WithdrawStatusEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static WithdrawStatusEnum findByBId(String code) {
		for (WithdrawStatusEnum t : WithdrawStatusEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}