package com.ebeijia.zl.shop.dao.member.service;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 会员信息表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomMemberService extends IService<TbEcomMember> {

    /**
     * 查询会员信息（高级查询）
     * @param ecomMember
     * @return
     */
    List<TbEcomMember> getMemberInfList(TbEcomMember ecomMember);
}
