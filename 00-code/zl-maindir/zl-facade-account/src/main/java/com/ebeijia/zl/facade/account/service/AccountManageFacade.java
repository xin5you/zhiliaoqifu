package com.ebeijia.zl.facade.account.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 
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
@Api(value = "账户实现接口")
public interface AccountManageFacade {

	
	/**
	* @Description:创建账户
	* @param:BaseAccountReq 账户开户请求参数
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 上午9:21:32 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	@ApiOperation(value = "开户接口", notes = "")
	BaseResult createAccount(AccountOpenReqVo req) throws Exception;
	


}
