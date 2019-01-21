package com.ebeijia.zl.facade.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.github.pagehelper.PageInfo;

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
public interface AccountQueryFacade {

	
	/**
	 * 
	* @Description: 账户信息查询
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:05:04 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	List<AccountVO> getAccountInfList(AccountQueryReqVo req) throws AccountBizException;

	/**
	 * 
	* @Description: 账户信息分页查询
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:16:11 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	PageInfo<AccountVO> getAccountInfPage(int startNum, int pageSize, AccountQueryReqVo req) throws AccountBizException;


	/**
	 *
	 * @Description: 账户账单分页查询
	 *
	 * @param:描述1描述
	 *
	 * @version: v1.0.0
	 * @author: zhuqi
	 * @date: 2018年12月14日 下午2:16:11
	 *
	 * Modification History:
	 * Date         Author          Version
	 *-------------------------------------*
	 * 2018年12月14日     zhuqi           v1.0.0
	 */
	PageInfo<AccountLogVO> getAccountLogPage(int startNum, int pageSize, AccountQueryReqVo req) throws AccountBizException;

	/**
	 * 当前交易记录明细
	 * @param req
	 * @return
	 * @throws AccountBizException
	 */
	AccountLogVO getAccountLogVoByParams(AccountQueryReqVo req)throws AccountBizException;


	/**
	 * 查询账户余额
	 * @return
	 */
	BigDecimal getAccountInfAccBal(String userType,String userChnlId,String userChnl,String bId);
}
