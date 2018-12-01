package com.ebeijia.zl.common.utils.domain;

import java.io.Serializable;

/**
* 
* 
* @ClassName: BaseEntity.java
* @Description: 基础实体类，包含各实体公用属性
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月29日 下午4:47:28 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月29日     zhuqi           v1.0.0
*/

public class BaseEntity implements Serializable {

	

	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据状态
	 */
	private String dataStat;
	
	/**
	 * 备注
	 */
	private String remarks;
	
	/**
	 * 创建人
	 */
	private String createUser;
	
	/**
	 * 创建时间 格式:20181129163111000
	 */
	private Long createTime;
	
	/**
	 * 修改人
	 */
	private String updateUser;
	
	/**
	 * 修改时间 格式:20181129163111000
	 */
	private Long updateTime;

	/**
	 * 乐观锁版本号
	 */
	private Integer lockVersion=0;

	public String getDataStat() {
		return dataStat;
	}

	public void setDataStat(String dataStat) {
		this.dataStat = dataStat;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}
	
}
