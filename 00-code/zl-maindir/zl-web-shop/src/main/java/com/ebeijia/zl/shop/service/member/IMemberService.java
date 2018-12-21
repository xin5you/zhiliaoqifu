package com.ebeijia.zl.shop.service.member;

import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.vo.AddressInfo;

public interface IMemberService {

    TbEcomMember createMember();

    Integer newAddress(AddressInfo address, Integer pos);

    PersonInf getMemberInfo();

    AddressInfo listAddress();

}
