package com.cn.thinkx.ecom.front.api.black.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.front.api.black.domain.WithDrawBlacklistInf;
import com.cn.thinkx.ecom.front.api.black.mapper.WithDrawBlacklistInfMapper;
import com.cn.thinkx.ecom.front.api.black.service.WithDrawBlacklistInfService;

@Service("withDrawBlacklistInfService")
public class WithDrawBlacklistInfServiceImpl implements WithDrawBlacklistInfService {
	
	@Autowired
	private WithDrawBlacklistInfMapper withDrawBlacklistInfMapper;

	@Override
	public WithDrawBlacklistInf getWithDrawBlacklistInfByUserId(String userId) {
		return withDrawBlacklistInfMapper.getWithDrawBlacklistInfByUserId(userId);
	}
}
