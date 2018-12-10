package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.MpAccount;
import com.ebeijia.zl.basics.wechat.mapper.MpAccountMapper;
import com.ebeijia.zl.basics.wechat.service.MpAccountService;

/**
 *
 * 微信账户表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class MpAccountServiceImpl extends ServiceImpl<MpAccountMapper, MpAccount> implements MpAccountService{
	
	@Autowired
	private MpAccountMapper mpAccountMapper;

	public MpAccount getByAccount(String account) {
		return mpAccountMapper.getByAccount(account);
	}

	@Override
	public MpAccount getSingleAccount() {
		return mpAccountMapper.getSingleAccount();
	}

	public List<MpAccount> listForPage(MpAccount searchEntity) {
		return mpAccountMapper.listForPage(searchEntity);
	}


}
