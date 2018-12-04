package com.ebeijia.zl.service.account.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.ebeijia.zl.facade.account.req.OpenAccountReq;
import com.ebeijia.zl.facade.account.service.TxnAccountInfFacade;
import com.ebeijia.zl.service.account.service.ITransLogService;
import com.ebeijia.zl.service.user.service.IUserInfService;


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

	@Autowired
	private ITransLogService transLogService;
	
	
	@Autowired
	private IUserInfService userInfService;
	
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
	public String createAccount(OpenAccountReq req) throws Exception {
		
		return null;
	}

	
	public String changeAccountStatus(OpenAccountReq req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
