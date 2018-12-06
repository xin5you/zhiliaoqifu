package com.ebeijia.zl.common.utils.enums;
/**
 *
 * 交易类型
 *
 */
public enum TransCode {
	
	MB10("B10", "商户消费"),
	MB20("B20", "商户充值"),
	MB80("B80", "商户开户"),
	MB40("B40", "商户转账"),
	MB50("B50", "企业员工充值"),
	MB90("B90", "商户收款"), 
	
	CW80("W80", "企业员工开户"),
	CW81("W81", "密码重置"),
	CW10("W10", "消费"),
	CW71("W71", "快捷消费"),
	CW11("W11", "退款"),
	CW74("W74", "退款（快捷）"),
	
	CW90("W90", "权益转让"),
	CW91("W91", "员工收款");
	

	private String code;

	private String value;

	TransCode(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static TransCode findByCode(String code) {
		for (TransCode t : TransCode.values()) {
			if (t.getCode().equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}
