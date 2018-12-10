package com.cn.thinkx.ecom.front.api.member.service;


import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.ebeijia.zl.common.utils.domain.BaseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberService {

	BaseResult<Object> insertMember(HttpServletRequest req, HttpServletResponse resp, MemberInf entity);
	
	MemberInf getMemberInfByPrimaryKey(String memberId);
}
