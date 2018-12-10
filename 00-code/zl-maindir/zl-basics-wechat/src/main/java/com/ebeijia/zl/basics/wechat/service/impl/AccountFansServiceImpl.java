package com.ebeijia.zl.basics.wechat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.basics.wechat.domain.AccountFans;
import com.ebeijia.zl.basics.wechat.enums.AccountFansStatusEnum;
import com.ebeijia.zl.basics.wechat.enums.FansStatusEnum;
import com.ebeijia.zl.basics.wechat.enums.MsgType;
import com.ebeijia.zl.basics.wechat.mapper.AccountFansMapper;
import com.ebeijia.zl.basics.wechat.service.AccountFansService;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 微信客户粉丝表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class AccountFansServiceImpl extends ServiceImpl<AccountFansMapper, AccountFans> implements AccountFansService{

	@Autowired
	private AccountFansMapper accountFansMapper;


	public AccountFans getByOpenId(String openId) {
		return accountFansMapper.getByOpenId(openId);
	}

	public List<AccountFans> list(AccountFans searchEntity) {
		return accountFansMapper.list(searchEntity);
	}

	public PageInfo<AccountFans> getAccountFansPage(int startNum, int pageSize,AccountFans entity) {
		
		PageHelper.startPage(startNum, pageSize);
		List<AccountFans> list = this.list(entity);
		PageInfo<AccountFans> page = new PageInfo<AccountFans>(list);
		return page;

	}

	public AccountFans getLastOpenId() {
		return accountFansMapper.getLastOpenId();
	}



	public void deleteByOpenId(String openId) {
		accountFansMapper.deleteByOpenId(openId);
	}
	
	/**
	 * 用户关注或者取消关注时候，同步数据
	 */
	public void syncAccountFans(String openId,String subscribeStatus){
		AccountFans accountFans=accountFansMapper.getByOpenId(openId);
		if(accountFans==null){
			accountFans=new AccountFans();
			accountFans.setOpenId(openId);
			accountFans.setStatus(AccountFansStatusEnum.TRUE_STATUS.getCode());
			accountFans.setFansStatus(FansStatusEnum.Fans_STATUS_00.getCode()); //粉丝菜单权限
			accountFans.setSubscribestatus(AccountFansStatusEnum.TRUE_STATUS.getCode());
			accountFans.setSubscribeTime(DateUtil.COMMON_FULL.getDateText(new Date()));
			accountFansMapper.add(accountFans);
		}
		else{
			if(MsgType.SUBSCRIBE.getName().equals(subscribeStatus)){//如果是关注状态
				accountFans.setStatus(AccountFansStatusEnum.TRUE_STATUS.getCode());
				accountFans.setSubscribestatus(AccountFansStatusEnum.TRUE_STATUS.getCode());
			}else{
				accountFans.setStatus(AccountFansStatusEnum.FALSE_STATUS.getCode());
				accountFans.setSubscribestatus(AccountFansStatusEnum.FALSE_STATUS.getCode());
			}
			accountFans.setSubscribeTime(DateUtil.COMMON_FULL.getDateText(new Date()));
			accountFansMapper.updateAccountFansByStatus(accountFans);//更新关注状态
		}
	}
	
	/***
	 * 修改商户粉丝表 微信客户端修改状态，不修改日期可用
	 * @param searchEntity
	 * @return
	 */
	public int updateAccountFansByMcht(AccountFans entity){
		return accountFansMapper.updateAccountFansByMcht(entity);
	}
}
