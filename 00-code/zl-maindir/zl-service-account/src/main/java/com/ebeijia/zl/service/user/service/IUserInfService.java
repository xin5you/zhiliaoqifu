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
	
	/**
	* 
	* @Description: 用户注册
	*
	* @param:userType 用户注册类型
	* @param:userName 用户名
	* @param:companyId 所属企业
	* @param:mobilePhone 手机号
	* @param:cardType 证件类型
	* @param:cardNo  证件号
	* @param:transId 交易类型
	* @param:transChnl 交易渠道

	* 
	* @return userId
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 上午10:43:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public String registerUserInf(String userType,String userName,String companyId,String mobilePhone,String cardType,String cardNo,String transId,String transChnl);
	
	boolean save(UserInf entity);
	
	/**
	 * 
	* @Description: 查找用戶
	*
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
	* @Description: 外部渠道號 查找用戶信息
	*
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
