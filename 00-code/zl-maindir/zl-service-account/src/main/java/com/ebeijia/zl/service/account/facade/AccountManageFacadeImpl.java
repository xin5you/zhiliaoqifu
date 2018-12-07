package com.ebeijia.zl.service.account.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.facade.account.vo.IntfaceTransLog;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import com.ebeijia.zl.service.user.service.IUserInfService;


/**
* 
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

@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
@Service
public class AccountManageFacadeImpl implements AccountManageFacade {
	
	private final  Logger log = LoggerFactory.getLogger(AccountManageFacadeImpl.class);

	@Autowired
	private ITransLogService transLogService;
	
	
	@Autowired
	private IUserInfService userInfService;
	
	@Autowired
	private IIntfaceTransLogService intfaceTransLogService;
	
	
	/**
	* 
	* @Description: 创建账户
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
	public BaseResult createAccount(AccountOpenReqVo req) throws Exception {
		
		log.info("==> 账户开户 mehtod=createAccount and OpenAccountReq={}",JSONArray.toJSON(req));
		
		/**
		 * 注册用户信息
		 */
		String userId=userInfService.registerUserInf(req.getUserType(),
										req.getUserName(),
										req.getCompanyId(),
										req.getMobilePhone(), 
										null, 
										req.getIcardNo(), 
										req.getTransId(), 
										req.getTransChnl(),
										req.getUserChnl(),
										req.getUserChnlId());
		
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		intfaceTransLog=intfaceTransLogService.newItfTransLog(req.getDmsRelatedKey(), userId, req.getTransId(), null, req.getUserType(), req.getTransChnl(),
				req.getUserChnl(),req.getUserChnlId(),null);
		intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志
		
		
		
		//执行开户操作
		intfaceTransLog.setBIds(req.getbIds());
		boolean eflag=transLogService.execute(intfaceTransLog); 
		
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}
}