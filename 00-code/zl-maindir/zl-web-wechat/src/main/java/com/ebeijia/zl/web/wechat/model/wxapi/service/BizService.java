package com.ebeijia.zl.web.wechat.model.wxapi.service;

import com.ebeijia.zl.basics.wechat.domain.AccountFans;
import com.ebeijia.zl.core.wechat.process.MpAccount;
import com.ebeijia.zl.core.wechat.vo.MsgRequest;


/**
 * 我的微信服务接口，主要用于结合自己的业务和微信接口
 */
public interface BizService {
	
	//消息处理
	public String processMsg(MsgRequest msgRequest,MpAccount mpAccount);
	
	
	//获取用户列表
	public boolean syncAccountFansList(MpAccount mpAccount);
	
	//获取单个用户信息
	public AccountFans syncAccountFans(String openId, MpAccount mpAccount, boolean merge);
	
	//根据openid 获取粉丝，如果没有，同步粉丝
	public AccountFans getFansByOpenId(String openid,MpAccount mpAccount);
	
	//移动用户分组
	public boolean updateFansGroupId(String openid,String togroupid,MpAccount mpAccount);
}



