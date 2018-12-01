package com.ebeijia.zl.service.account.facade;

import com.ebeijia.zl.facade.account.req.BaseAccountReq;
import com.ebeijia.zl.facade.account.service.TxnAccountInfFacade;


/**
 * 
* 
* @ClassName: TxnAccountInfServiceImpl.java
* @Description: 账户开户操作
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 下午1:58:49 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public class TxnAccountInfFacadeImpl implements TxnAccountInfFacade {

	
	/**
	 * 
	* @Function: TxnAccountInfServiceImpl.java
	* @Description: 创建账户
	*
	* @param:BaseAccountReq 账户开户请求参数
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
	public String createAccount(BaseAccountReq req) throws Exception {
		// TODO 判断用户是否存在
		// TODO 判断企业是否存在
		// TODO 企业员工绑定企业
		// TODO 专项账户开户操作
		return null;
	}

	
	public String changeAccountStatus(BaseAccountReq req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
