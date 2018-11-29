package com.cn.thinkx.ecom.basics.member.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

public class MemberInf extends BaseEntity{

	private static final long serialVersionUID = -7655982014086839784L;
	/*
	 * 会员ID
	 */
	private String membeId;
	/*
	 * 个人信息ID
	 */
	private String personId;
	/*
	 * 会员USER_ID
	 */
	private String userId;
	/*
	 * open_id
	 */
	private String openId;
	/*
	 * 会员等级ID
	 */
	private String lvId;
	/*
	 * 会员积分
	 */
	private String point;
	
	private String personalName;	//用户名
	private String mobilePhoneNo;	//手机号码
	
	public String getPersonalName() {
		return personalName;
	}
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	public String getMembeId() {
		return membeId;
	}
	public void setMembeId(String membeId) {
		this.membeId = membeId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getLvId() {
		return lvId;
	}
	public void setLvId(String lvId) {
		this.lvId = lvId;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	@Override
	public String toString() {
		return "MemberInf [membeId=" + membeId + ", personId=" + personId + ", userId=" + userId + ", openId=" + openId
				+ ", lvId=" + lvId + ", point=" + point + ", personalName=" + personalName + ", mobilePhoneNo="
				+ mobilePhoneNo + "]";
	}
}
