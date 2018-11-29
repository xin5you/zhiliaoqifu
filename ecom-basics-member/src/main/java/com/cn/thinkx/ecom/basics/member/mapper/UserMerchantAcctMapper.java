package com.cn.thinkx.ecom.basics.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.UserMerchantAcct;

@Mapper
public interface UserMerchantAcctMapper {

	/**
	 * 获取客户的卡产品信息 所属的机构和 商户信息
	 * 
	 * @param openid 微信openid
	 * @return
	 */
	List<UserMerchantAcct> getUserMerchantAcctByUser(UserMerchantAcct entity);
}
