package com.ebeijia.zl.service.account.service;

import java.util.List;

import com.ebeijia.zl.facade.account.vo.AccountInf;
import com.ebeijia.zl.facade.user.vo.UserInf;

/**
* 
* @ClassName: AccountInfService.java
* @Description: 专项账户service
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 下午2:42:07 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
*/
public interface AccountInfService {

	/**
	 * 
	* @Function: AccountInfService.java
	* @Description: accountNo穫取賬戶信息
	*
	* @param:accountNo 賬戶號
	* @return：AccountInf 賬戶信息
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午2:47:42 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	AccountInf getAccountInfById(String  accountNo);
	
	/**
	 * 
	* @Function: AccountInfService.java
	* @Description: 通過userId穫取賬戶信息
	*
	* @param: userId 用戶Id
	* @param: userType 用戶類型
	* @param: bId 專項類型
	* @return：AccountInf 賬戶信息
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午2:51:11 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	AccountInf getAccountInfByUserId(String userId,String userType,String bId);
	
	/**
	 * 
	* @Function: AccountInfService.java
	* @Description: 用戶開戶（一個用戶開一個專項賬戶）
	*
	* @param user 用戶信息
	* @param:accountInf 賬戶信息
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午2:53:14 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	int createAccountInf(UserInf user,AccountInf accountInf);
	
	
	/**
	 * 
	* @Function: AccountInfService.java
	* @Description: 用戶開戶（一個用戶開多個專項賬戶）
	*
	* @param:user
	* @param:accountList
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午3:00:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	int createAccountInf(UserInf user,List<AccountInf> accountList);
	
	/**
	 * 
	* @Function: AccountInfService.java
	* @Description: 批量賬戶開戶 （多個用戶開多個專項賬戶）
	*
	* @param users 用戶列表
	* @param:accountList 賬戶信息列表
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午2:54:26 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	int batchUserAccountInf(List<UserInf> users,List<AccountInf> accountList);
}
