package com.cn.thinkx.ecom.front.api.black.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.front.api.black.domain.WithDrawBlacklistInf;

@Mapper
public interface WithDrawBlacklistInfMapper {

	WithDrawBlacklistInf getWithDrawBlacklistInfByUserId(@Param("userId")String userId);
	
}
