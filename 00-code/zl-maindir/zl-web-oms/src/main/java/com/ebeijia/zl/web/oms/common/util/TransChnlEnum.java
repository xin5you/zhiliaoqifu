package com.ebeijia.zl.web.oms.common.util;

/**
 * 交易渠道号枚举
 * @author Administrator
 *
 */
public enum TransChnlEnum {

	CHANNEL0("10001001", "管理平台"),
	CHANNEL1("40001010", "微信公众号"),
	CHANNEL2("40002001", "微信公众号"),
	CHANNEL3("40003001", "支付宝"),
	CHANNEL4("40004001", "嘉福"),
	CHANNEL5("40005001", "网银"),
	CHANNEL6("40006001", "电商"),
	CHANNEL7("40001002", "通卡"),
	CHANNEL8("40007001", "话费充值"),
	CHANNEL9("40008001", "卡券集市"),
	CHANNEL10("40007002", "流量充值"),
	CHANNEL11("40009001", "组合支付");

	private String code;
	private String name;

	TransChnlEnum(String code, String name){
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public static TransChnlEnum findByCode(String code) {
		for (TransChnlEnum t : TransChnlEnum.values()) {
			if (t.code.equalsIgnoreCase(code)) {
				return t;
			}
		}
		return null;
	}
}