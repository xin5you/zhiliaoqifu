package com.ebeijia.zl.service.account.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.vo.AccountInf;
import com.ebeijia.zl.facade.account.vo.TransLog;


/**
 *
 * 用户账户信息 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
public interface IAccountInfService extends IService<AccountInf> {
	
	
	/**
	 * 
	* @Function: IAccountInfService.java
	* @Description: 查找用户的ID
	*
	* @param:userId 用户Id
	* @param:bId 专项类型Id
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午6:44:06 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	AccountInf getAccountInfByUserId(String userId,String bId);

	/**
	 * 
	* @Function: IAccountInfService.java
	* @Description: 账户操作
	*
	* @param:voList 交易流水列表
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午5:00:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	boolean execute(List<TransLog> voList) throws Exception;
	
	
}
