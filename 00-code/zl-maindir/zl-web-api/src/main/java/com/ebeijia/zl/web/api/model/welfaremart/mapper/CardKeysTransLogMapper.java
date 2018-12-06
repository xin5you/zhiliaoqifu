package com.ebeijia.zl.web.api.model.welfaremart.mapper;

import java.util.List;
import java.util.Map;

import com.ebeijia.zl.web.api.model.welfaremart.model.CardKeysTransLog;

public interface CardKeysTransLogMapper {
	
	/**
	 * 获取主键
	 * @param paramMap
	 */
	void getPrimaryKey(Map<String, String> paramMap);
	
	CardKeysTransLog getCardKeysTransLogByCard(CardKeysTransLog cardKeysTransLog);
	
	int insertCardKeysTransLog(CardKeysTransLog cardKeysTransLog);
	
	int updateCardKeysTransLog(CardKeysTransLog cardKeysTransLog);
	
	int insertBatchCardKeysTransLogList(List<CardKeysTransLog> cardKeysTransLogList);
	
	int getCountCardKeys(CardKeysTransLog cardKeysTransLog);
	
	List<CardKeysTransLog> getCardKeysTransLogByOrderId(CardKeysTransLog ckt);
	
}
