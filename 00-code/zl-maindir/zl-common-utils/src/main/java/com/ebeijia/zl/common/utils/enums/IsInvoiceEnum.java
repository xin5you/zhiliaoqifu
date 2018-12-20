package com.ebeijia.zl.common.utils.enums;

/**
 * 开票标识
 * @author Administrator
 *
 */
public enum IsInvoiceEnum {

	INVOICE_TRUE("1", "已开票"),
	INVOICE_FALSE("0", "未开票");

	private String code;
	private String name;

	IsInvoiceEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static IsInvoiceEnum findByBId(String code) {
		for (IsInvoiceEnum t : IsInvoiceEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}