package com.ebeijia.zl.facade.user.service;

/**
 * 
* 
* @ClassName: UserFacade.java
* @Description: 用户信息管理接口
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年11月30日 下午1:38:31 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public interface UserFacade {

	/**
	 * 
	* @Function: UserFacade.java
	* @Description: 该函数的功能描述
	*
	* @param:userId 用户Id
	* @return：UserInf 用户对象
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午1:39:20 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
//	UserInf getUserInfByUserId(String userId);
	
	/**
	 * 
	* @Function: UserFacade.java
	* @Description: 外部渠道获取用户信息
	*
	* @param:描述1描述
	* @return：返回结果描述
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 下午1:40:52 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
//	UserInf getUserInfByExternalId(String externalId,String channel);
	
	
	/**
	 * 根据手机号查找个人信息 适用于用户注册绑定
	 * @param phoneNo
	 * @param channel 渠道标识
	 * @return
	 */
//	PersonInf getPersonInfByPhoneNo(String phoneNo,String channel);
	
	
	/**
	 * 根据手机号查找用户信息 适用于用户注册绑定
	 * @param phoneNo
	 * @param userName
	 * @return
	 */
//	UserInf getUserInfByPhoneNo(String phoneNo,String channel);
}
