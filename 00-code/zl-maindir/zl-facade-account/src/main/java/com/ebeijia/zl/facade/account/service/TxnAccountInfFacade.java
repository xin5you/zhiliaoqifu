package com.ebeijia.zl.facade.account.service;

import com.ebeijia.zl.facade.account.req.BaseAccountReq;

/**
 * 
* 
* @ClassName: ZLAccountInfFacade.java
* @Description: 账户开户接口
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 上午10:02:09 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public interface TxnAccountInfFacade {

	
	/**
	 * 
	* @Function: ZLAccountInfFacade.java
	* @Description: 创建账户
	*
	* @param:BaseAccountReq 账户开户请求参数
	* @return：返回结果描述
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午10:11:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	String createAccount(BaseAccountReq req) throws Exception;
	

	
	/**
	 * 
	* @Function: ZLAccountInfFacade.java
	* @Description: 账户状态变更
	*
	* @param:描述1描述
	* @return：返回结果描述
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午10:57:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	String changeAccountStatus(BaseAccountReq req)throws Exception;

}
