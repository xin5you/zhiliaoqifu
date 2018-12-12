package com.ebeijia.zl.basics.member.service;

import com.ebeijia.zl.basics.member.domain.AccountFans;

public interface AccountFansService {

	AccountFans getByOpenId(String openId);
}
