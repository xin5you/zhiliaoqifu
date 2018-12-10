package com.ebeijia.zl.basics.wechat.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.wechat.domain.AccountFans;
import com.github.pagehelper.PageInfo;


/**
 *
 * 微信客户粉丝表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface AccountFansService extends IService<AccountFans> {
	
	

	public AccountFans getByOpenId(String openId);
	

	public List<AccountFans> list(AccountFans searchEntity);

	
	public PageInfo<AccountFans> getAccountFansPage(int startNum, int pageSize, AccountFans accountFans);
	

	public AccountFans getLastOpenId();



	public void deleteByOpenId(String openId);
	
	/**
	 * 用户关注或者取消关注时候，同步数据
	 */
	public void syncAccountFans(String openId,String subscribeStatus);
	
	/***
	 * 修改商户粉丝表 微信客户端修改状态，不修改日期可用
	 * @param searchEntity
	 * @return
	 */
	public int updateAccountFansByMcht(AccountFans entity);
	

}
