package com.ebeijia.zl.service.account.facade;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.req.AccountOpenReqVo;
import com.ebeijia.zl.facade.account.service.AccountManageFacade;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import com.ebeijia.zl.service.user.service.IUserInfService;

import java.util.ArrayList;
import java.util.List;


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


@com.alibaba.dubbo.config.annotation.Service()
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
		intfaceTransLog=intfaceTransLogService.newItfTransLog(intfaceTransLog,req.getDmsRelatedKey(), userId, req.getTransId(), null, req.getUserType(), req.getTransChnl(),
				req.getUserChnl(),req.getUserChnlId(),null);
		intfaceTransLog.setTransDesc("开户");
		intfaceTransLog.setMchntCode(req.getCompanyId());
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);  //保存接口处交易日志
		


		//执行开户操作
		intfaceTransLog.setBIds(req.getbIds());
		boolean eflag=transLogService.execute(intfaceTransLog); 
		
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}


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
	@ApiOperation(value = "批量开户接口", notes = "")
	public BaseResult createAccountList(String dmsRelatedKey,String transChnl,String userType,List<AccountOpenReqVo> list) throws Exception {

		log.info("==> 账户开户 mehtod=createAccount and createAccountList={}", JSONArray.toJSON(list));

		IntfaceTransLog intfaceTrans = intfaceTransLogService.getItfTransLogDmsChannelTransId(dmsRelatedKey, transChnl);
		if(intfaceTrans!=null){
			return ResultsUtil.error("99", "重复交易");
		}
		intfaceTrans = intfaceTransLogService.newItfTransLog(intfaceTrans, dmsRelatedKey, dmsRelatedKey, TransCode.MB50.getCode(), null, userType, transChnl,
				null,null, null);
		intfaceTrans.setTransDesc("批量开户");

		/**
		 * 注册用户信息
		 */
		List<IntfaceTransLog> intfaceTransLogs = new ArrayList<IntfaceTransLog>();
		for (AccountOpenReqVo req : list) {
			String userId = userInfService.registerUserInf(req.getUserType(),
					req.getUserName(),
					req.getCompanyId(),
					req.getMobilePhone(),
					null,
					req.getIcardNo(),
					req.getTransId(),
					req.getTransChnl(),
					req.getUserChnl(),
					req.getUserChnlId());
			IntfaceTransLog intfaceTransLog = intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
			intfaceTransLog = intfaceTransLogService.newItfTransLog(intfaceTransLog, req.getDmsRelatedKey(), userId, req.getTransId(), null, req.getUserType(), req.getTransChnl(),
					req.getUserChnl(), req.getUserChnlId(), null);
			intfaceTransLog.setTransDesc("开户");
			intfaceTransLog.setMchntCode(req.getCompanyId());
			intfaceTransLog.setBIds(req.getbIds());

			intfaceTransLogs.add(intfaceTransLog);
		}
		intfaceTransLogService.saveBatch(intfaceTransLogs);  //保存接口处交易日志

		//执行开户操作
		boolean eflag = transLogService.executeList(intfaceTransLogs);

		intfaceTransLogService.updateById(intfaceTrans,eflag);
		return new BaseResult<>(intfaceTrans.getRespCode(),null,intfaceTrans.getItfPrimaryKey());
	}
}
