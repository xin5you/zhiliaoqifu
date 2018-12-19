package com.ebeijia.zl.common.utils.enums;

import java.util.HashSet;
import java.util.Set;

public enum SpecAccountTypeEnum{
	
	A00("A00", "通用账户","A"), 
	A01("A01", "托管账户","A"),
	
	B01("B01", "办公用品","B"),
	B02("B02", "差旅专用","B"),
	B03("B03", "体检专用","B"), 
	B04("B04", "培训专用","B"),
	B05("B05", "食品专用","B"),
	B06("B06", "通讯专用","B"), 
	B07("B07", "保险专用","B"),
	B08("B08", "交通专用","B");

	
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

	public static Set<String>  getList() {
		Set<String> set	=new HashSet<String>();
			for (SpecAccountTypeEnum t : SpecAccountTypeEnum.values()) {
				set.add(t.bId);
			}
			return set;
		}
}