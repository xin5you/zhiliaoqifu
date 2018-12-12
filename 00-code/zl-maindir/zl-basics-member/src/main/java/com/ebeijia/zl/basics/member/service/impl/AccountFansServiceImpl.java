package com.ebeijia.zl.basics.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.member.domain.AccountFans;
import com.ebeijia.zl.basics.member.mapper.AccountFansMapper;
import com.ebeijia.zl.basics.member.service.AccountFansService;

@Service
public class AccountFansServiceImpl implements AccountFansService {
	
	@Autowired
	private AccountFansMapper accountFansMapper;

	@Override
	public AccountFans getByOpenId(String openId) {
		return accountFansMapper.getByOpenId(openId);
	}

}
