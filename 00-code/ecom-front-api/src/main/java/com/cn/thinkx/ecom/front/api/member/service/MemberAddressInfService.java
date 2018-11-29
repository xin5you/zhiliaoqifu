package com.cn.thinkx.ecom.front.api.member.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface MemberAddressInfService {

	/**
	 * 添加会员的收货地址
	 * 
	 * @param req
	 * @param resp
	 * @param memberAddre
	 * @return
	 */
	BaseResult<Object> addMemberAddress(String address, String addressId, MemberAddress memberAddre);

	/**
	 * 修改会员的收货地址
	 * 
	 * @param req
	 * @param resp
	 * @param memberAddre
	 * @return
	 */
	BaseResult<Object> updateMemberAddress(String address, String addressId, MemberAddress memberAddre);

	/**
	 * 查询该会员下的所有收货地址（参数：会员id）
	 * 
	 * @param memberAddre
	 * @return
	 */
	List<MemberAddress> getMemberAddressList(MemberAddress memberAddre);

	/**
	 * 查询会员收货的默认地址
	 * 
	 * @param userId
	 * @return
	 */
	MemberAddress getMemberDefAddr(String memberId);

	/**
	 * 删除会员的收货地址
	 * 
	 * @return
	 */
	BaseResult<Object> deleteMemberAddress(String addrId);

	/**
	 * 修改会员的默认地址
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	// BaseResult<Object> updateMemberDefAddr(String addrId);

	/**
	 * 通过主键查询收货地址
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	MemberAddress getMemberAddressByAddrId(String addrId);

	/**
	 * 通过会员id查看会员得收货地址个数
	 * 
	 * @param memberId
	 * @return
	 */
	int getMemberAddressByMemberIdCount(String memberId);
}
