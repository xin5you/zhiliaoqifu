package com.ebeijia.zl.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.member.domain.PersonInf;

@Mapper
public interface PersonInfMapper {
	
	/**
	 * 根据openid查询用户
	 * @param openid
	 * @return
	 */
	PersonInf getPersonInfByOpenId(String openid);
}
