package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.AccountMenuGroup;
import com.github.pagehelper.PageInfo;


/**
 *
 * 微信菜单组 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface AccountMenuGroupService extends IService<AccountMenuGroup> {
	
	public List<AccountMenuGroup> list(AccountMenuGroup accountMenuGroup);

	
	public PageInfo<AccountMenuGroup> getAccountMenuGroupPage(int startNum, int pageSize, AccountMenuGroup accountMenuGroup);
	
	/***
	 * 获取分组
	 * @return
	 */
	public AccountMenuGroup getMembersGroupsId();

}
