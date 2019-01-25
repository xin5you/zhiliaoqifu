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
	MB90("B90", "商户提款"),//提现
	MB95("B95", "商户收单"),
	MB96("B96", "商户退款"),

	CW80("W80", "企业员工开户"),
	CW81("W81", "密码重置"),
	CW10("W10", "商品消费"),
	CW50("W50", "员工充值"),
	CW71("W71", "快捷支付"),
	CW20("W20", "购买代金券"),
	CW11("W11", "退款"),
	CW74("W74", "微信退款"),
	CW40("W40", "员工转账"),
	CW90("W90", "权益转让"),
	CW91("W91", "提款申请"), //提现
	CW92("W92", "解冻提交"),
	CW93("W93", "解冻撤销");


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
