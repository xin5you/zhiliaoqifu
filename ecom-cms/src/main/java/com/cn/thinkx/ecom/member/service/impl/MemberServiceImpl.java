package com.cn.thinkx.ecom.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.basics.member.mapper.MemberInfMapper;
import com.cn.thinkx.ecom.member.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberInfMapper memberInfMapper;

	@Override
	public PageInfo<MemberInf> getMemberListPage(int startNum, int pageSize, MemberInf entity) {
		PageHelper.startPage(startNum, pageSize);
		List<MemberInf> memberInfList = memberInfMapper.getList(entity);
		PageInfo<MemberInf> userPage = new PageInfo<MemberInf>(memberInfList);
		return userPage;
	}

}
