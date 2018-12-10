package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.AccountMenuGroup;
import com.ebeijia.zl.basics.wechat.mapper.AccountMenuGroupMapper;
import com.ebeijia.zl.basics.wechat.service.AccountMenuGroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 微信菜单组 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class AccountMenuGroupServiceImpl extends ServiceImpl<AccountMenuGroupMapper, AccountMenuGroup> implements AccountMenuGroupService{


	@Autowired
	private AccountMenuGroupMapper accountMenuGroupMapper;



	public List<AccountMenuGroup> list(AccountMenuGroup searchEntity) {
		return accountMenuGroupMapper.list(searchEntity);
	}

	public PageInfo<AccountMenuGroup> getAccountMenuGroupPage(int startNum, int pageSize,AccountMenuGroup entity) {
		PageHelper.startPage(startNum, pageSize);
		List<AccountMenuGroup> list = this.list(entity);
		PageInfo<AccountMenuGroup> page = new PageInfo<AccountMenuGroup>(list);
		return page;
	}

	
	public AccountMenuGroup getMembersGroupsId() {
		return accountMenuGroupMapper.getMembersGroupsId();
	}
}
