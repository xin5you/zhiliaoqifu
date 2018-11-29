package com.cn.thinkx.ecom.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.domain.UserInf;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface MemberInfMapper extends BaseDao<MemberInf> {

	/**
	 * 通过用户id、用户信息id、openId查询会员信息
	 * 
	 * @return
	 */
	MemberInf getMemberInfByUserId(MemberInf entity);
	
	/**
	 * 通过openId查找会员信息
	 * @param openId
	 * @return
	 */
	UserInf getUserInfByOpenId(String openId);
	

}
