package com.ebeijia.zl.basics.member.service;

import java.util.List;

import com.ebeijia.zl.basics.member.domain.UserMerchantAcct;

public interface UserMerchantAcctService {

	/**
	 * 获取客户的卡产品信息 所属的机构和 商户信息
	 * 
	 * @param openid 微信openid
	 * @return
	 */
	List<UserMerchantAcct> getUserMerchantAcctByUser(UserMerchantAcct entity);
}
