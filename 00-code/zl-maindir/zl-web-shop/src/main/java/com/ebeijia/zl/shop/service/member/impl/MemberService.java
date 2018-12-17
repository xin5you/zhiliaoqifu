package com.ebeijia.zl.shop.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMemberAddress;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
import com.ebeijia.zl.shop.service.member.IMemberService;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class MemberService implements IMemberService {
    @Autowired
    HttpSession session;

    @Autowired
    ITbEcomMemberService memberService;

    @Override
    public TbEcomMember createMember() {
        return null;
    }

    @Override
    public Integer newAddress(AddressInfo address, Integer pos) {
        return null;
    }

    @Override
    public MemberInfo getMemberInfo() {
        String userId = (String) session.getAttribute("userId");
        if (StringUtils.isEmpty(userId)){
            throw new BizException(401,"超时了，请重新登录");
        }
        TbEcomMember m = new TbEcomMember();
        m.setMemberId(userId);
        m.setDataStat("0");
        TbEcomMember one = memberService.getOne(new QueryWrapper<>(m));
        if (one == null){
            throw new BizException(404,"用户不存在");
        }
        //TODO 对接账务接口

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setPersonalId(one.getPersonId());
        memberInfo.setCompanyName("知了");
        memberInfo.setMobilePhoneNo("13812341234");
        memberInfo.setUserId(one.getUserId());
        memberInfo.setPersonalName("李狗子");
        return memberInfo;
    }

    @Override
    public TbEcomMemberAddress listAddress() {
        return null;
    }
}
