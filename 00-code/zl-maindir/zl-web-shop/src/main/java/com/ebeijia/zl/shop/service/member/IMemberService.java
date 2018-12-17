package com.ebeijia.zl.shop.service.member;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMemberAddress;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;

public interface IMemberService {

    TbEcomMember createMember();

    Integer newAddress(AddressInfo address, Integer pos);

    MemberInfo getMemberInfo();

    TbEcomMemberAddress listAddress();

}
