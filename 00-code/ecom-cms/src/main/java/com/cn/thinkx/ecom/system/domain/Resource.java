package com.cn.thinkx.ecom.system.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class Resource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 创建时间
	 */
	private String createDateTime; 
	/**
	 * 名称
	 */
	private String name; 
	/**
	 * 菜单路径
	 */
	private String url; 
	/**
	 * 描述
	 */
	private String description; 
	/**
	 * 图标
	 */
	private String icon;  
	/**
	 * 排序号
	 */
	private String seq; 
	/**
	 * 资源类型, 0菜单 1功能
	 */
	private String resourceType; 
	/**
	 * 状态 0启用 1停用
	 */
	private String state;  
	/**
	 * 父级
	 */
	private String pid; 
	
	private String key;
	
	//标识（1：会显示该条数据，代表角色可以选择此Resource信息。0：则相反）
	private String checked;

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
