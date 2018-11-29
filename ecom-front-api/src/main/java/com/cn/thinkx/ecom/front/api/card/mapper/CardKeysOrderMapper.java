package com.cn.thinkx.ecom.front.api.card.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardKeysOrderMapper {

	int getCardKeysOrderCount(String userId);
}
