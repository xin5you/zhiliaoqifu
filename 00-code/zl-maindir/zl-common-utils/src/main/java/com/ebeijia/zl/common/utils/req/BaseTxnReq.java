package com.ebeijia.zl.common.utils.req;

import java.util.Set;

import com.sun.istack.internal.NotNull;

/**
 * 
* 
* @ClassName: FacadeReqVo.java
* @Description: facade远程调用请求参数工具类
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月29日 下午5:20:44 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月29日     zhuqi           v1.0.0
 */
public class BaseTxnReq implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	/**
	 * 交易类型
	 */
	private String transId;
	
	/**
	 * 渠道号
	 */
	private String transChnl;
	
	/**
	 * 用Id 企业Id 供应商Id 分销商Id;根据userType来区分
	 */
	private String userId;
	
	/**
	 * 专用账户类型id
	 */
	private Set<String> bIds;
	
	
	/**
	 * 100：企业员工账户
	 * 200：企业账户
	 * 300：供应商账户
	 * 400：分销商账户
	 */
	
	private String userType;
	
	/**
	 * 渠道請求訂單號
	 */
	private String dmsRelatedKey;


	public String getTransId() {
		return transId;
	}


	public void setTransId(String transId) {
		this.transId = transId;
	}


	public String getTransChnl() {
		return transChnl;
	}


	public void setTransChnl(String transChnl) {
		this.transChnl = transChnl;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	public Set<String> getbIds() {
		return bIds;
	}


	public void setbIds(Set<String> bIds) {
		this.bIds = bIds;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}


	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}
}
