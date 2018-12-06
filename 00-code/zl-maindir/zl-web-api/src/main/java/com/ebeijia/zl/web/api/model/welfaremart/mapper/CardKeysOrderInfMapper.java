package com.ebeijia.zl.web.api.model.welfaremart.mapper;

import org.apache.ibatis.annotations.Param;

import com.ebeijia.zl.web.api.model.welfaremart.model.CardKeysOrderInf;

public interface CardKeysOrderInfMapper {
	
	int getCardKeysOrderByCardKeys(@Param("cardKey")String cardKey);
	
	CardKeysOrderInf getCardKeysOrderByOrderId(@Param("orderId")String orderId);
	
	CardKeysOrderInf getOrderNumByOrderId(CardKeysOrderInf cko);
	
	int getMonthResellNum(@Param("userId")String userId);
	
	int insertCardKeysOrderInf(CardKeysOrderInf cardKeysOrderInf);
	
	int updateCardKeysOrderInf(CardKeysOrderInf cardKeysOrderInf);
	
	CardKeysOrderInf getOrderFailByUserIdAndOrderId(CardKeysOrderInf cko);
	
}
