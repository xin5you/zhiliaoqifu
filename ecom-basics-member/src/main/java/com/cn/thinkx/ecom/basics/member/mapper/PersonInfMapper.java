package com.cn.thinkx.ecom.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.PersonInf;

@Mapper
public interface PersonInfMapper {
	
	/**
	 * 根据openid查询用户
	 * @param openid
	 * @return
	 */
	PersonInf getPersonInfByOpenId(String openid);
}
