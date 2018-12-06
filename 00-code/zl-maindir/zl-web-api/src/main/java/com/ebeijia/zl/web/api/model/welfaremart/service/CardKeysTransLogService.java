package com.ebeijia.zl.web.api.model.welfaremart.service;

import java.util.List;

import com.ebeijia.zl.web.api.model.welfaremart.model.CardKeysTransLog;

public interface CardKeysTransLogService {
	
	/**
	 * 获取主键
	 * @param paramMap
	 */
	String getPrimaryKey();
	
	CardKeysTransLog getCardKeysTransLogByCard(CardKeysTransLog cardKeysTransLog);
	
	int insertCardKeysTransLog(CardKeysTransLog cardKeysTransLog) throws Exception;
	
	int updateCardKeysTransLog(CardKeysTransLog cardKeysTransLog) throws Exception;
	
	int insertBatchCardKeysTransLogList(List<CardKeysTransLog> cardKeysTransLogList) throws Exception;
	
	List<CardKeysTransLog> getCardKeysTransLogByOrderId(CardKeysTransLog ckt);
	
}
