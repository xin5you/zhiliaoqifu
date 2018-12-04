package com.ebeijia.zl.common.utils.enums;

/**
 * 数据启用标记
 * @author zhuqi
 *
 */
public enum DataStatEnum{

	TRUE_STATUS("0"),
	FALSE_STATUS("1");

	private String code;

	DataStatEnum(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}

}