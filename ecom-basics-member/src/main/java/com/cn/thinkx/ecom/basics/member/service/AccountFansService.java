package com.cn.thinkx.ecom.basics.member.service;

import com.cn.thinkx.ecom.basics.member.domain.AccountFans;

public interface AccountFansService {

	AccountFans getByOpenId(String openId);
}
