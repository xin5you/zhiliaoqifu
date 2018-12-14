package com.ebeijia.zl.service.account.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import com.ebeijia.zl.service.user.service.IUserInfService;
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

	@Override
	public List<AccountVO> getAccountInfList(AccountQueryReqVo req) throws Exception {
		
		return null;
	}

	@Override
	public PageInfo<AccountVO> getAccountInfPage(int startNum, int pageSize, AccountQueryReqVo req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
}
