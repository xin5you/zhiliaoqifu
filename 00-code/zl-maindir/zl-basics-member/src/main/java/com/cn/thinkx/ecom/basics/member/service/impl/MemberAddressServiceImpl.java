package com.cn.thinkx.ecom.basics.member.service.impl;

import com.cn.thinkx.ecom.basics.member.domain.MemberAddress;
import com.cn.thinkx.ecom.basics.member.mapper.MemberAddressMapper;
import com.cn.thinkx.ecom.basics.member.service.MemberAddressService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("memberAddressService")
public class MemberAddressServiceImpl extends BaseServiceImpl<MemberAddress> implements MemberAddressService {

	@Autowired
	private MemberAddressMapper memberAddressMapper;

	@Override
	public List<MemberAddress> getMemberAddressList(MemberAddress entity) {
		return memberAddressMapper.getList(entity);
	}

	@Override
	public MemberAddress getMemberDefAddr(String memberId) {
		return memberAddressMapper.getMemberDefAddr(memberId);
	}

	@Override
	public int getMemberAddressByMemberIdCount(String memberId) {
		return memberAddressMapper.getMemberAddressByMemberIdCount(memberId);
	}

}
