package com.ebeijia.zl.shop.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMemberAddress;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberAddressService;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
import com.ebeijia.zl.shop.service.member.IMemberService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
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
    ITbEcomMemberService memberDao;

    @Autowired
    ITbEcomMemberAddressService addressDao;
//    @Autowired
//    UserInfFacade userInfFacade;

    @Override
    public TbEcomMember createMember() {
        return null;
    }

    @Override
    public Integer newAddress(AddressInfo address, Integer pos) {
        String memberId = (String) session.getAttribute("memberId");
        TbEcomMemberAddress temp = new TbEcomMemberAddress();
        temp.setMemberId(memberId);
        //检查是否存在地址
        TbEcomMemberAddress memberAddress = addressDao.getOne(new QueryWrapper<>(temp));

        temp.setShipAddressName(address.getRecipient());
        temp.setMobile(address.getPhoneNo());
        temp.setRegion(address.getCounty());
        temp.setCity(address.getCity());
        temp.setProvince(address.getProvince());
        temp.setAddrDetail(address.getAddress());
        if (memberAddress == null) {
            memberAddress = temp;
            memberAddress.setAddrId(IdUtil.getNextId());
            //TODO Get UserName
            memberAddress.setUserName("");
            boolean save = addressDao.save(memberAddress);
            if (!save) {
                throw new AdviceMessenger(500, "数据插入失败，请重试");
            }
        } else {
            boolean update = addressDao.update(temp, new QueryWrapper<>(memberAddress));
            if (!update) {
                throw new AdviceMessenger(500, "数据插入失败，请重试");
            }
        }
        return 200;
    }

    @Override
    public MemberInfo getMemberInfo() {
        String memberId = (String) session.getAttribute("memberId");
        if (StringUtils.isEmpty(memberId)) {
            throw new BizException(401, "超时了，请重新登录");
        }
        TbEcomMember m = new TbEcomMember();
        m.setMemberId(memberId);
        m.setDataStat("0");
        TbEcomMember one = memberDao.getOne(new QueryWrapper<>(m));
        if (one == null) {
            throw new BizException(404, "用户不存在");
        }
        //TODO 对接账务接口

//        UserInf userInf = userInfFacade.getUserInfByUserId(one.getUserId());
//        userInfFacade.getPersonInfByPhoneNo("","");
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setPersonalId(one.getPersonId());
        memberInfo.setMobilePhoneNo("13812341234");
        memberInfo.setUserId(one.getUserId());
        memberInfo.setPersonalName("李狗子");
        return memberInfo;
    }

    @Override
    public AddressInfo listAddress() {
        String memberId = (String) session.getAttribute("memberId");
        TbEcomMemberAddress temp = new TbEcomMemberAddress();
        temp.setMemberId(memberId);
        //检查是否存在地址
        TbEcomMemberAddress memberAddress = addressDao.getOne(new QueryWrapper<>(temp));
        return new AddressInfo(memberAddress);
    }
}
