package com.ebeijia.zl.service.account.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.req.AccountTransactionReq;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.account.vo.IntfaceTransLog;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.account.service.IIntfaceTransLogService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import com.ebeijia.zl.service.user.service.IPersonInfService;
import com.ebeijia.zl.service.user.service.IUserInfService;


/**
* 
* @Description: 账户交易操作
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



/**
 * 
* 
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月5日 上午11:52:48 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月5日     zhuqi           v1.0.0
 */
public class AccountTransactionFacadeImpl implements AccountTransactionFacade {
	
	
	private final  Logger log = LoggerFactory.getLogger(AccountTransactionFacadeImpl.class);

	@Autowired
	private ITransLogService transLogService;
	
	@Autowired
	private IUserInfService userInfService;
	
	@Autowired
	private IPersonInfService personInfService;
	
	@Autowired
	private IIntfaceTransLogService intfaceTransLogService;
	

	/**
	* 
	* @Description: 账户充值（单一账户类型充值）
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月5日 上午11:52:48 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月5日     zhuqi           v1.0.0
	 */
	public BaseResult executeRechargeByOneBId(AccountTransactionReq req) throws Exception {
		
		log.info("==>  账户充值 mehtod=AccountTransactionReq and AccountTransactionReq={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		/**
		 * 商户充值
		 */
		if (TransCode.MB20.getCode().equals(req.getTransId())){
			
			UserInf toUserInf= userInfService.getUserInfByUserName(req.getToCompanyId());
			
			if(toUserInf==null){
				return ResultsUtil.error("99", "用户或企业未开户");
			}
			/****实例化接口流水****/
			intfaceTransLog=intfaceTransLogService.newItfTransLog(req.getDmsRelatedKey(), toUserInf.getUserId(), req.getTransId(), req.getTfrInBId(), req.getUserType(), req.getTransChnl(),null);
			intfaceTransLogService.addBizItfTransLog(
					intfaceTransLog, 
					req.getTransAmt(),
					req.getUploadAmt(), 
					null,
					null, 
					null, 
					null,
					null,
					null,
					null,
					null);
			
		}else if(TransCode.MB50.getCode().equals(req.getTransId())){
			//企业员工充值
			PersonInf personInf=personInfService.getPersonInfByPhoneNo(req.getMobilePhone());
		
			UserInf user= userInfService.getUserInfByUserName(req.getFromCompanyId());
			
			if(personInf==null || user==null){
				return ResultsUtil.error("99", "用户或企业未开户");
			}
			
			/****实例化接口流水****/
			intfaceTransLog=intfaceTransLogService.newItfTransLog(req.getDmsRelatedKey(), personInf.getUserId(), req.getTransId(), null, req.getUserType(), req.getTransChnl(),null);
			intfaceTransLogService.addBizItfTransLog(
													intfaceTransLog, 
													req.getTransAmt(),
													req.getUploadAmt(), 
													null,
													null, 
													null, 
													req.getTfrInUserId(),
													req.getTfrInBId(),
													req.getTfrOutUserId(),
													req.getTfrOutBId(),
													null);
		}
		
		intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志
		
		//执行操作
		boolean eflag=transLogService.execute(intfaceTransLog); 
		
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}

	
	public BaseResult executeRecharge(List list) throws Exception {
		
		return null;
		
	}

	/**
	* @Description: 消费接口
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:08:55 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	public BaseResult executeConsume(AccountTransactionReq req) throws Exception{
		
		log.info("==>  账户消費 mehtod=AccountTransactionReq and AccountTransactionReq={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(req.getDmsRelatedKey(), req.getUserId(), req.getTransId(), null, req.getUserType(), req.getTransChnl(),null);
		intfaceTransLogService.addBizItfTransLog(
												intfaceTransLog, 
												req.getTransAmt(),
												req.getUploadAmt(), 
												null,
												null, 
												null, 
												null,
												null,
												null,
												null,
												null);
		/****保存接口易流水****/
		intfaceTransLog.setRemarks(JSONObject.toJSONString(req.getTransList())); //保存消费类型信息
		intfaceTransLogService.save(intfaceTransLog);
		
		//执行操作
		boolean eflag=transLogService.execute(intfaceTransLog);
		
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}

	/**
	* @Description: 转账操作
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年11月30日 上午11:08:55 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年11月30日     zhuqi           v1.0.0
	 */
	public BaseResult executeTransfer() {
		return null;
	}
	

}
