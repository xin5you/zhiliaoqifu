package com.ebeijia.zl.facade.account.req;

/**
 * 
* 
* @Description: 账户信息查询列表
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月14日 下午2:07:50 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月14日     zhuqi           v1.0.0
 */
public class AccountQueryReqVo implements java.io.Serializable {

	private static final long serialVersionUID = -5097911751512172022L;

	/**
	 * 100：企业员工账户
	 * 200：企业账户
	 * 300：供应商账户
	 * 400：分销商账户
	 */
	
	private String userType;
	
	/**
	 * 用户外部渠道标识
	 */
	private String userChnlId;
	
	/**
	 * 用户所属渠道
	 */
	private String userChnl;
	
	/**
	 * 专项账户Id
	 */
	private String bId;
	
	

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getbId() {
		return bId;
	}

	public void setbId(String bId) {
		this.bId = bId;
	}
	
}
