package com.cn.thinkx.ecom.member.service;

import com.cn.thinkx.ecom.basics.member.domain.MemberInf;
import com.github.pagehelper.PageInfo;

public interface MemberService {

	/**
	 * 查询所有会员信息
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<MemberInf> getMemberListPage(int startNum, int pageSize, MemberInf entity);
}
