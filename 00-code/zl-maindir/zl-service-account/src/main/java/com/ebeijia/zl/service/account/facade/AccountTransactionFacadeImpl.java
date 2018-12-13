package com.ebeijia.zl.service.account.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountRefundReqVo;
import com.ebeijia.zl.facade.account.req.AccountTransferReqVo;
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
@Configuration
@com.alibaba.dubbo.config.annotation.Service(interfaceName="accountTransactionFacade")
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
	public BaseResult executeRechargeByOneBId(AccountRechargeReqVo req){
		
		log.info("==>  账户充值 mehtod=executeRechargeByOneBId and AccountRechargeReqVo={}",JSONArray.toJSON(req));
		
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
			
			UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
			
			if(toUserInf==null){
				return ResultsUtil.error("99",String.format("当前企业{%s}所属渠渠道{%s}未开户", req.getTransChnl(),req.getUserChnl()));
			}

			/****实例化接口流水****/
			intfaceTransLog=intfaceTransLogService.newItfTransLog(req.getDmsRelatedKey(), toUserInf.getUserId(), req.getTransId(),req.getPriBId(),
																  req.getUserType(), req.getTransChnl(),req.getUserChnl(),req.getTransChnl(),null);
			intfaceTransLogService.addBizItfTransLog(
					intfaceTransLog, 
					req.getTransAmt(),
					req.getUploadAmt(), 
					null,
					null, 
					null, 
					null,
					null,
					req.getUserChnl(),
					req.getUserChnlId(),
					null);
			
		}else if(TransCode.MB50.getCode().equals(req.getTransId())){
			//企业员工充值
			PersonInf personInf=personInfService.getPersonInfByPhoneNo(req.getMobilePhone());
		
			UserInf fromUser= userInfService.getUserInfByUserName(req.getFromCompanyId());
			
			if(personInf==null || fromUser==null){
				return ResultsUtil.error("99", "用户或企业未开户");
			}
			
			/****实例化接口流水****/
			intfaceTransLog=intfaceTransLogService.newItfTransLog(
					req.getDmsRelatedKey(),
					personInf.getUserId(), 
					req.getTransId(), 
					null, 
					req.getUserType(), 
					req.getTransChnl(),req.getUserChnl(),req.getUserChnlId(),null);
			intfaceTransLogService.addBizItfTransLog(
													intfaceTransLog, 
													req.getTransAmt(),
													req.getUploadAmt(), 
													null,
													null, 
													null, 
													personInf.getUserId(),
													req.getPriBId(),
													fromUser.getUserId(),
													SpecAccountTypeEnum.A00.getCode(),
													null);
		}
		
		intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志
		
		//执行操作
		boolean eflag=false; 
	     try {  
	    	 eflag=transLogService.execute(intfaceTransLog); 
	      } catch (AccountBizException accountBizException) {  
	           
	           return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
	      } 
		
		
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
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
	public BaseResult executeRecharge(List list) throws Exception {
		
		//TODO 批量用户充值
		
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
	public BaseResult executeConsume(AccountConsumeReqVo req) throws Exception{
		
		log.info("==>  账户消費 mehtod=executeConsume and AccountConsumeReqVo={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		/**获取用户数据*/
		UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
		
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				req.getDmsRelatedKey(),
				toUserInf.getUserId(),
				req.getTransId(),
				null, 
				req.getUserType(), 
				req.getTransChnl(),
				req.getUserChnl(),
				req.getUserChnlId(),null);
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
		
		//保存消费类型信息 重要数据存储 重要 重要
		intfaceTransLog.setRemarks(JSONObject.toJSONString(req.getTransList())); 
		
		/****保存接口易流水****/
		intfaceTransLogService.save(intfaceTransLog);
		
		//执行操作
		boolean eflag=false; 
	     try {  
	    	 eflag=transLogService.execute(intfaceTransLog); 
	      } catch (AccountBizException accountBizException) {  
	           
	           return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
	      } 
		
		
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
	public BaseResult executeTransfer(AccountTransferReqVo req) throws Exception{
		log.info("==>  账户转账 mehtod=executeTransfer and AccountTransferReqVo={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		UserInf fromUserInf= null;
		UserInf toUserInf=null; 
		
		//企业账户转账 通过企业Id转账到企业
		if (TransCode.MB40.getCode().equals(req.getTransId())){
			fromUserInf= userInfService.getUserInfByUserName(req.getTfrOutUserId());
			toUserInf =userInfService.getUserInfByUserName(req.getTfrInUserId());

		}else{
			//TODO 员工转账开发
		}
		
		if(fromUserInf==null ){
			return ResultsUtil.error("99", "账户信息不存在{%s}"+req.getTfrOutUserId());
		}
		if(toUserInf==null ){
			return ResultsUtil.error("99", "账户信息不存在{%s}"+req.getTfrInUserId());
		}
		
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				req.getDmsRelatedKey(),
				req.getUserId(), req.getTransId(), 
				null, req.getUserType(),
				req.getTransChnl(),req.getUserChnl(),req.getUserChnlId(),null);
		intfaceTransLogService.addBizItfTransLog(
												intfaceTransLog, 
												req.getTransAmt(),
												req.getUploadAmt(), 
												null,
												null, 
												null, 
												toUserInf.getUserId(),
												req.getTfrInBId(),
												fromUserInf.getUserId(),
												req.getTfrOutBId(),
												null);
		
	//保存转账类型信息 重要数据存储 重要 重要
	intfaceTransLog.setRemarks(req.getTfrOutUserId()+","+req.getTfrOutBId()+"-->"+req.getTfrInUserId()+","+req.getTfrInBId());
	
	intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志
	
	//执行操作
	boolean eflag=false; 
     try {  
    	 eflag=transLogService.execute(intfaceTransLog); 
      } catch (AccountBizException accountBizException) {  
           return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
      } 
	
	
	//修改当前接口请求交易状态
	intfaceTransLogService.updateById(intfaceTransLog,eflag);
	return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}


	@Override
	public BaseResult executeRefund(AccountRefundReqVo req) throws Exception {
		
		log.info("==>  账户转账 mehtod=executeRefund and AccountRefundReqVo={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(null, req.getTransChnl());
		return null;
	}
	

}
