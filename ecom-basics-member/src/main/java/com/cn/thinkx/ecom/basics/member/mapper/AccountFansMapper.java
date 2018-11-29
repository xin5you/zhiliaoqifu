package com.cn.thinkx.ecom.basics.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.member.domain.AccountFans;

@Mapper
public interface AccountFansMapper {

	AccountFans getByOpenId(String openId);
}
