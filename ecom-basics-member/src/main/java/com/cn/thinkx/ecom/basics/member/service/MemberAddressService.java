package com.cn.thinkx.ecom.basics.member.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface MemberAddressService extends BaseService<MemberAddress> {

	List<MemberAddress> getMemberAddressList(MemberAddress entity);

	/**
	 * 查询会员收货的默认地址
	 * 
	 * @param userId
	 * @return
	 */
	MemberAddress getMemberDefAddr(String memberId);

	/**
	 * 通过会员id查看会员得收货地址个数
	 * 
	 * @param memberId
	 * @return
	 */
	int getMemberAddressByMemberIdCount(String memberId);
}
