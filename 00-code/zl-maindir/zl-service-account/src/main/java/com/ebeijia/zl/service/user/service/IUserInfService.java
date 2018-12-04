package com.ebeijia.zl.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.user.vo.UserInf;


/**
 *
 * 用户信息 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
public interface IUserInfService extends IService<UserInf> {
	
	
	boolean save(UserInf entity);
	
	/**
	 * 
	* @Function: IUserInfService.java
	* @Description: 查找用戶
	*
	* @param:描述1描述
	* @return：返回结果描述
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:06:50 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	UserInf getUserInfByPhoneNo( String phoneNo, String channel);

	/**
	 * 
	* @Function: IUserInfService.java
	* @Description: 外部渠道號 查找用戶信息
	*
	* @param:描述1描述
	* @return：返回结果描述
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午8:07:31 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	UserInf getUserInfByExternalId (String externalId,String channel);
}
