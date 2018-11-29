package com.cn.thinkx.ecom.basics.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.PersonInf;
import com.cn.thinkx.ecom.basics.member.mapper.PersonInfMapper;
import com.cn.thinkx.ecom.basics.member.service.PersonInfService;

@Service
public class PersonInfServiceImpl implements PersonInfService {
	
	@Autowired
	private PersonInfMapper personInfMapper;

	@Override
	public PersonInf getPersonInfByOpenId(String openid) {
		return personInfMapper.getPersonInfByOpenId(openid);
	}

	
}
