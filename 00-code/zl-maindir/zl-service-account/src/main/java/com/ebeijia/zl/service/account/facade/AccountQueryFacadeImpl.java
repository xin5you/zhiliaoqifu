package com.ebeijia.zl.service.account.facade;

import java.util.List;

import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.service.account.service.IAccountLogService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import com.github.pagehelper.PageInfo;


/**
* 
* @Description: 账户查询
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

@Configuration
@com.alibaba.dubbo.config.annotation.Service(interfaceName="accountQueryFacade")
public class AccountQueryFacadeImpl implements AccountQueryFacade {
	
	private final  Logger log = LoggerFactory.getLogger(AccountQueryFacadeImpl.class);
	
	@Autowired
	private IAccountInfService accountInfService;

	@Autowired
	private IAccountLogService accountLogService;

	@Override
	public List<AccountVO> getAccountInfList(AccountQueryReqVo req) throws AccountBizException {
		return accountInfService.getAccountInfList(req);
	}

	@Override
	public PageInfo<AccountVO> getAccountInfPage(int startNum, int pageSize, AccountQueryReqVo req) throws AccountBizException {
		PageHelper.startPage(startNum, pageSize);
		List<AccountVO> list = getAccountInfList(req);
		PageInfo<AccountVO> page = new PageInfo<AccountVO>(list);
		return page;
	}

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
	@Override
	public PageInfo<AccountLogVO> getAccountLogPage(int startNum, int pageSize, AccountQueryReqVo req) throws AccountBizException{
		PageHelper.startPage(startNum, pageSize);
		List<AccountLogVO> list = accountLogService.getAccountLogVoList(req);
		PageInfo<AccountLogVO> page = new PageInfo<AccountLogVO>(list);
		return page;
	}

	/**
	 * 当前交易记录明细
	 * @param req
	 * @return
	 * @throws AccountBizException
	 */
	@Override
	public AccountLogVO getAccountLogVoByParams(AccountQueryReqVo req)throws AccountBizException{
		if(StringUtils.isEmpty(req.getActPrimaryKey())){
			return null;
		}
		return accountLogService.getAccountLogVoList(req).get(0);
	}
}
