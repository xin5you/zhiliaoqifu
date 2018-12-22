package com.ebeijia.zl.common.utils.req;

import java.util.Set;


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

	private static final long serialVersionUID = 5539906702603802177L;
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
	 * 渠道请求订单号 全渠道唯一
	 */
	private String dmsRelatedKey;
	
	/**
	 * 用户外部渠道标识
	 */
	private String userChnlId;
	
	/**
	 * 用户所属渠道
	 */
	private String userChnl;
	
	
    /**
     * 交易描述
     */
    private String transDesc;
    
    
    /**
     * 交易数量
     */
    private int transNumber=1;


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


	public String getUserChnlId() {
		return userChnlId;
	}


	public void setUserChnlId(String userChnlId) {
		this.userChnlId = userChnlId;
	}


	public String getUserChnl() {
		return userChnl;
	}


	public void setUserChnl(String userChnl) {
		this.userChnl = userChnl;
	}


	public String getTransDesc() {
		return transDesc;
	}


	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}


	public int getTransNumber() {
		return transNumber;
	}


	public void setTransNumber(int transNumber) {
		this.transNumber = transNumber;
	}
}
