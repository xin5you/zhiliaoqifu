package com.cn.thinkx.ecom.system.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Integer id;
	/**
	 * 角色名称
	 */
	private String name; 
	/**
	 * 排序号
	 */
	private Integer seq; 
	/**
	 * 是否默认
	 */
	private String isdefault; 
	/**
	 * 备注
	 */
	private String description; 
	
	//标识（1：会显示该条数据，代表用户可以选择此RRole信息。0：则相反）
	private String checked;

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
