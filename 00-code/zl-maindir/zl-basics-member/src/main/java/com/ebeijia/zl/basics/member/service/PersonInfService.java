package com.ebeijia.zl.basics.member.service;

import com.ebeijia.zl.basics.member.domain.PersonInf;

public interface PersonInfService {

	/**
	 * 根据openid查询用户
	 * @param openid
	 * @return
	 */
	PersonInf getPersonInfByOpenId(String openid);
}
