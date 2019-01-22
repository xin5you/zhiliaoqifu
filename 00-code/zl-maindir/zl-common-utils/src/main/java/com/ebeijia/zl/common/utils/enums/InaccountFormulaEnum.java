package com.ebeijia.zl.common.utils.enums;

/**
 * 上账金额计算方式
 * @author Administrator
 *
 */
public enum InaccountFormulaEnum {

	InaccountFormulaEnum_1("1", "企业收款金额 = （供应商上账金额 * （1 + 供应商费率）） / (1 + 供应商费率 + 企业费率)"),
	InaccountFormulaEnum_2("2", "企业收款金额 = （供应商上账金额 * （1 + 供应商费率）） * (1 - 供应商费率 - 企业费率)");

	private String code;
	private String name;

	InaccountFormulaEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static InaccountFormulaEnum findByBId(String code) {
		for (InaccountFormulaEnum t : InaccountFormulaEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}