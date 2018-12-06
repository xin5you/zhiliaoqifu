package com.ebeijia.zl.facade.account.service;

import java.util.List;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.account.req.AccountTransactionReq;

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
public interface AccountTransactionFacade {

	/**
	* 
	* @Description: 账户充值(针对单一类型)
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
	BaseResult executeRechargeByOneBId(AccountTransactionReq req) throws Exception;
	
	
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
	BaseResult executeRecharge(List<AccountTransactionReq> list) throws Exception;
	
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
	BaseResult executeConsume(AccountTransactionReq req) throws Exception;

	
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
	BaseResult executeTransfer();
}
