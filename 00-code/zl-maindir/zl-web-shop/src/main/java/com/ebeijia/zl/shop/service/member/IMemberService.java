package com.ebeijia.zl.shop.service.member;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;

public interface IMemberService {

    TbEcomMember createMember();

    Integer newAddress(String token, String address, Integer pos);
}
