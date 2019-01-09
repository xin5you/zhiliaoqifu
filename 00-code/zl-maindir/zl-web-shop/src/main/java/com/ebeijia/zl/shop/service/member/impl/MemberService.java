package com.ebeijia.zl.shop.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.facade.user.service.UserInfFacade;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMemberAddress;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberAddressService;
import com.ebeijia.zl.shop.dao.member.service.ITbEcomMemberService;
import com.ebeijia.zl.shop.service.member.IMemberService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements IMemberService {

    @Autowired
    ShopUtils shopUtils;

    @Autowired
    ITbEcomMemberService memberDao;

    @Autowired
    ITbEcomMemberAddressService addressDao;

    @Autowired
    UserInfFacade userInfFacade;

    @Override
    public TbEcomMember createMember() {
        return null;
    }

    @Override
    public Integer newAddress(AddressInfo address, Integer pos) {
        MemberInfo memberInfo = shopUtils.getSession();
        String memberId = memberInfo.getMemberId();

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

        //如果没有则添加
        if (memberAddress == null) {
            memberAddress = temp;
            memberAddress.setAddrId(IdUtil.getNextId());
            //TODO Get UserName
//            memberAddress.setUserName();
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
    public PersonInf getMemberInfo() {
        MemberInfo memberInfo = shopUtils.getSession();
        String memberId = memberInfo.getMemberId();

        TbEcomMember m = new TbEcomMember();
        m.setMemberId(memberId);
        m.setDataStat("0");
        TbEcomMember one = memberDao.getOne(new QueryWrapper<>(m));
        if (one == null) {
            throw new BizException(404, "用户不存在");
        }
        //对接账务接口
        PersonInf personInf = userInfFacade.getPersonInfByPhoneNo(memberInfo.getMobilePhoneNo());
        if (personInf==null){
            throw new BizException(500, "通讯故障");
        }
        return personInf;
    }

    @Override
    public AddressInfo listAddress() {
        MemberInfo memberInfo = shopUtils.getSession();
        String memberId = memberInfo.getMemberId();

        TbEcomMemberAddress temp = new TbEcomMemberAddress();
        temp.setMemberId(memberId);
        //检查是否存在地址
        TbEcomMemberAddress memberAddress = addressDao.getOne(new QueryWrapper<>(temp));
        if (memberAddress==null){
            throw new AdviceMessenger(ResultState.OK,"请输入收货地址");
        }
        return new AddressInfo(memberAddress);
    }




}
