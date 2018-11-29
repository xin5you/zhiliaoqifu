package com.cn.thinkx.ecom.basics.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.AccountFans;
import com.cn.thinkx.ecom.basics.member.mapper.AccountFansMapper;
import com.cn.thinkx.ecom.basics.member.service.AccountFansService;

@Service
public class AccountFansServiceImpl implements AccountFansService {
	
	@Autowired
	private AccountFansMapper accountFansMapper;

	@Override
	public AccountFans getByOpenId(String openId) {
		return accountFansMapper.getByOpenId(openId);
	}

}
