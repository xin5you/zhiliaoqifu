package com.ebeijia.zl.basics.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.member.domain.PersonInf;
import com.ebeijia.zl.basics.member.mapper.PersonInfMapper;
import com.ebeijia.zl.basics.member.service.PersonInfService;

@Service
public class PersonInfServiceImpl implements PersonInfService {
	
	@Autowired
	private PersonInfMapper personInfMapper;

	@Override
	public PersonInf getPersonInfByOpenId(String openid) {
		return personInfMapper.getPersonInfByOpenId(openid);
	}

	
}
