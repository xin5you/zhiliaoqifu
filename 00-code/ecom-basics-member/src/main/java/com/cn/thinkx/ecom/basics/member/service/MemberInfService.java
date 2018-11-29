package com.cn.thinkx.ecom.basics.member.service;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface MemberInfService extends BaseService<MemberInf> {

	/**
	 * 通过用户id、用户信息id、openId查询会员信息
	 * 
	 * @return
	 */
	MemberInf getMemberInfByUserId(MemberInf entity);
	
	/**
	 * 根据用户openId查找
	 * @param openId
	 * @return
	 */
	MemberInf getMemberInfByOpenId(String openId);
	
	/**
	 * 知了企服用户 注册成为电商会员
	 * @param openId
	 * @return
	 */
	MemberInf RegMemberInfByOpenId(String openId) throws Exception;
}
