package com.ebeijia.zl.common.utils.enums;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
* 
* @ClassName: AccountStatusEnum.java
* @Description: 账户状态. 适用于账户表
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月3日 下午6:59:16 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月3日     zhuqi           v1.0.0
 */
public enum AccountStatusEnum {

	/**
	 * 激活
	 */
	ACTIVE("激活", "00"), 
	/**
	 * 冻结
	 */
	INACTIVE("冻结", "10"),

	/**
	 * 注销
	 */
	CANCELLED("注销", "90");

	/** 枚举值 */
	private String value;

	/** 描述 */
	private String desc;

	private AccountStatusEnum(String desc, String value) {
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static AccountStatusEnum getEnum(String value) {
		AccountStatusEnum resultEnum = null;
		AccountStatusEnum[] enumAry = AccountStatusEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue() == value) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}
	
	public static Map<String, Map<String, Object>> toMap() {
		AccountStatusEnum[] ary = AccountStatusEnum.values();
		Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
		for (int num = 0; num < ary.length; num++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = String.valueOf(getEnum(ary[num].getValue()));
			map.put("value", String.valueOf(ary[num].getValue()));
			map.put("desc", ary[num].getDesc());
			enumMap.put(key, map);
		}
		return enumMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		AccountStatusEnum[] ary = AccountStatusEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getDesc());
			list.add(map);
		}
		return list;
	}
	
}
