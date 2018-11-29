package com.cn.thinkx.ecom.front.api.member.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.cn.thinkx.ecom.common.domain.BaseResult;

public interface MemberService {

	BaseResult<Object> insertMember(HttpServletRequest req, HttpServletResponse resp, MemberInf entity);
	
	MemberInf getMemberInfByPrimaryKey(String memberId);
}
