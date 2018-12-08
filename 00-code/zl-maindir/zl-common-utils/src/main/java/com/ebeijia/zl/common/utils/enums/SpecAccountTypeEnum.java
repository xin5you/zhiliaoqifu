package com.ebeijia.zl.common.utils.enums;

import java.math.BigDecimal;

public enum SpecAccountTypeEnum{
	
	A0("A0", "通用账户","A"), 
	A1("A1", "托管账户","A"),
	B1("B1", "办公用品","B"),
	B2("B2", "差旅专用","B"),
	B3("B3", "体检专用","B"), 
	B4("B4", "培训专用","B"),
	B5("B5", "食品专用","B"),
	B6("B6", "通讯专用","B"), 
	B7("B7", "保险专用","B"),
	B8("B8", "生日福利","B"),
	B9("B9", "年节福利","B");

	
	private String bId;
	
	private String name;
	
	private String code;
	

	public String getbId() {
		return bId;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	SpecAccountTypeEnum(String bId, String name,String code) {
		this.bId = bId;
		this.name = name;
		this.code = code;
	}
	
	public static SpecAccountTypeEnum findByBId(String bId) {
		for (SpecAccountTypeEnum t : SpecAccountTypeEnum.values()) {
			if (t.bId.equalsIgnoreCase(bId)) {
				return t;
			}
		}
		return null;
	}

}