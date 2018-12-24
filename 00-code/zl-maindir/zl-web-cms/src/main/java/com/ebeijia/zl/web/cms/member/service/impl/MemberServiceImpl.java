package com.ebeijia.zl.web.cms.member.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
import com.ebeijia.zl.shop.dao.member.mapper.TbEcomMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.web.cms.member.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Autowired
	private TbEcomMemberMapper memberInfMapper;

	public PageInfo<TbEcomMember> getMemberListPage(int startNum, int pageSize, TbEcomMember entity) {
		PageHelper.startPage(startNum, pageSize);
		Wrapper<TbEcomMember> wrapper=new QueryWrapper<>();
		List<TbEcomMember> memberInfList = memberInfMapper.selectList(wrapper);
		PageInfo<TbEcomMember> userPage = new PageInfo<TbEcomMember>(memberInfList);
		return userPage;
	}

}
