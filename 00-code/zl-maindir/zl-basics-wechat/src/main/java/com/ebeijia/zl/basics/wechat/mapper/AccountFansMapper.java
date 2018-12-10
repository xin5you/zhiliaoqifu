package com.ebeijia.zl.basics.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.basics.wechat.domain.AccountFans;

/**
 *
 * 微信客户粉丝表 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Mapper
public interface AccountFansMapper extends BaseMapper<AccountFans> {



	public AccountFans getByOpenId(String openId);
	
	public List<AccountFans> list(AccountFans searchEntity);

	public Integer getTotalItemsCount(AccountFans searchEntity);
	
	public AccountFans getLastOpenId();
	
	public void add(AccountFans entity);
	
	public void addList(List<AccountFans> list);

	public void deleteByOpenId(String openId);
	
	/**
	 * 修改商户粉丝表 关注 或取消关注时候，修改日期可用
	 * @param searchEntity
	 * @return
	 */
	public int updateAccountFansByStatus(AccountFans entity);
	
	/***
	 * 修改商户粉丝表 微信客户端修改状态，不修改日期可用
	 * @param searchEntity
	 * @return
	 */
	public int updateAccountFansByMcht(AccountFans entity);



}
