package com.ebeijia.zl.shop.service.member;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.vo.MemberInfo;

public interface IMemberService {

    TbEcomMember createMember();

    Integer newAddress(String token, String address, Integer pos);

    MemberInfo getMemberInfo();

}
