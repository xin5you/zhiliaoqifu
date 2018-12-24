package com.ebeijia.zl.web.cms.member.service;

import com.ebeijia.zl.shop.dao.member.domain.TbEcomMember;
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
	 PageInfo<TbEcomMember> getMemberListPage(int startNum, int pageSize, TbEcomMember entity);
}
