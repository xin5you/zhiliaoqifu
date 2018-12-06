package com.cn.thinkx.ecom.system.domain;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class Resource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String description; // 描述
	private String icon; // 图标
	private String resourceName; // 名称
	private String resourceType;// 资源类型, 0菜单 1功能
	private String loginType;
	private String url; // 菜单路径
	private Integer seq; // 排序号
	private String pid; // 父级
	private String resourceKey;
	
	//标识（1：会显示该条数据，代表角色可以选择此Resource信息。0：则相反）
	private String checked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

}
