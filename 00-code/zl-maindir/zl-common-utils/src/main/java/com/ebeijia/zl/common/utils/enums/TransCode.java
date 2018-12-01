package com.ebeijia.zl.common.utils.enums;
/**
 *
 * 交易类型
 *
 */
public enum TransCode {
	MB80("B80", "商户开户"), 
	MB81("B81", "企业开户"),
	MB82("B82", "供应商开户"),
	MB83("B83", "分销商开户"),
	MB10("B10", "商户提现"), 
	MB20("B20", "商户沉淀资金账户充值"),
	MB30("B30", "商户网站退货交易"),
	MB40("B40", "商户账户转账"),
	MB50("B50", "后台批量充值"),
	CW80("W80", "企业员工开户"),
	CW81("W81", "密码重置"),
	CW10("W10", "消费"),
	CW20("W20", "充值"),
	CW71("W71", "快捷消费"),
	CW11("W11", "退款"),
	CW74("W74", "退款（快捷）"),
	CW90("W90", "权益转让"),
	CW91("W91", "现金账户转移");

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
