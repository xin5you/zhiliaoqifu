package com.ebeijia.zl.web.cms.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.basics.member.domain.MemberInf;
import com.ebeijia.zl.basics.member.mapper.MemberInfMapper;
import com.ebeijia.zl.web.cms.member.service.MemberService;
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
