package com.ebeijia.zl.web.api.model.withdraw.service;

import com.ebeijia.zl.web.api.model.withdraw.domain.WithdrawOrderDetail;
import com.ebeijia.zl.web.api.model.withdraw.suning.vo.Content;

public interface WithdrawOrderDetailService {
	
	/**
	 * 获取主键
	 * @param paramMap
	 */
	String getPrimaryKey();
	
	int getCountBySerialNo(String serialNo);
	
	int insertWithdrawOrderDetail(WithdrawOrderDetail withdrawOrderDetail);
	
	int updateWithdrawOrderDetail(WithdrawOrderDetail withdrawOrderDetail);
	
	void YFBBatchWithdrawNotify(Content content) throws Exception;
	
	void YFBBatchWithdrawSendMsg(Content content) throws Exception;
	
	boolean YFBBatchWithdrawNotifyUpdateUserCardKey(String userId, String orderId);
	
}
