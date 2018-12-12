package com.ebeijia.zl.web.cms.system.domain;

import com.ebeijia.zl.common.utils.domain.BaseEntity;

public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String roleName; // 角色名称
	private String description; // 备注
	private String loginType;
	private Integer seq; // 排序号
	
	//标识（1：会显示该条数据，代表用户可以选择此RRole信息。0：则相反）
	private String checked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}


}
