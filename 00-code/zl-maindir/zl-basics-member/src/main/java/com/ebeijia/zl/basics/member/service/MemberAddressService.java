package com.ebeijia.zl.basics.member.service;


import com.ebeijia.zl.basics.member.domain.MemberAddress;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

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
