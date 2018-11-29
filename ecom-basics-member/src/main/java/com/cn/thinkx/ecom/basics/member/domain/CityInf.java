package com.cn.thinkx.ecom.basics.member.domain;

/**
 * 地区选择
 * 
 * @author kpplg
 *
 */
public class CityInf {

	private Long areaId; // 区域ID
	private String name; // 区域名称
	private Long areaClass; // 0-国家 1-省 2-市 3-区县 4-街道
	private Long parentId; // 父级Id

	public Long getAreaId() {
		return areaId;
	}

	public String getName() {
		return name;
	}

	public Long getAreaClass() {
		return areaClass;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAreaClass(Long areaClass) {
		this.areaClass = areaClass;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
