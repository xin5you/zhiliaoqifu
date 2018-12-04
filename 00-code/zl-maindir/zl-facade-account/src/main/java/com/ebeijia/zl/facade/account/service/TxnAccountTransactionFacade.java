package com.ebeijia.zl.facade.account.service;

import java.util.List;

/**
* @Description: 账户交易
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 上午10:59:39 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public interface TxnAccountTransactionFacade {

	/**
	 * 
	* @Description: 账户充值
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:00:28 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	void executeRecharge() throws Exception;
	
	
	/**
	 * 
	* @Description: 账户批量充值
	*
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:02:43 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	void executeRecharge(List list) throws Exception;
	
	/**
	* @Description: 消费接口
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:08:55 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	void executeConsume() throws Exception;
	
	/**
	 * 
	* @Function: TxnAccountTransactionFacade.java
	* @Description: 快捷消费
	*  用户快捷消费时，先从第三方扣款，回调通知平台
	*  平台发起调用快捷支付接口，先充值到通卡账户，再用通卡消费
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:04:58 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	void executeQuickConsume() throws Exception;
	
  /**
	* 
	* @Description: 转账
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:11:39 
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	void executeTransfer();
}
