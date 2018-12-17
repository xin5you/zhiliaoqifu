package com.ebeijia.zl.basics.member.mapper;

import com.ebeijia.zl.basics.member.domain.MemberInf;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberInfMapper extends BaseDao<MemberInf> {

	/**
	 * 通过用户id、用户信息id、openId查询会员信息
	 * 
	 * @return
	 */
	MemberInf getMemberInfByUserId(MemberInf entity);

}
