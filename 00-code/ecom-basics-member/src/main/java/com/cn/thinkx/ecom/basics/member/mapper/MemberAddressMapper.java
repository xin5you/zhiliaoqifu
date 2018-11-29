package com.cn.thinkx.ecom.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface MemberAddressMapper extends BaseDao<MemberAddress> {

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
