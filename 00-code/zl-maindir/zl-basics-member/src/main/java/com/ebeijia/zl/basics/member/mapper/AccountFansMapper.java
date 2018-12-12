package com.ebeijia.zl.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.member.domain.AccountFans;

@Mapper
public interface AccountFansMapper {

	AccountFans getByOpenId(String openId);
}
