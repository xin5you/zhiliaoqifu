package com.ebeijia.zl.service.account.facade;

import java.util.List;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.req.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.tools.ResultsUtil;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
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
	 * @Description: 账户充值
	 *
	 * @version: v1.0.0
	 * @author: zhuqi
	 * @date: 2018年12月19日 上午11:00:28
	 *
	 * Modification History:
	 * Date         Author          Version
	 *-------------------------------------*
	 * 2018年11月30日     zhuqi           v1.0.0
	 */
	public BaseResult executeRecharge(AccountRechargeReqVo req) throws Exception{
		log.info("==>  账户充值 mehtod=executeRecharge and AccountRechargeReqVo={}",JSONArray.toJSON(req));
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error("99", "重复交易");
		}
		UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
		if(toUserInf==null){
			return ResultsUtil.error("99",String.format("当前用户{%s}所属渠渠道{%s}未开户", req.getTransChnl(),req.getUserChnl()));
		}
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(intfaceTransLog,req.getDmsRelatedKey(), toUserInf.getUserId(), req.getTransId(),req.getPriBId(),
				req.getUserType(), req.getTransChnl(),req.getUserChnl(),req.getUserChnlId(),null);
		intfaceTransLogService.addBizItfTransLog(
				intfaceTransLog,
				req.getTransAmt(),
				req.getUploadAmt(),
				null,
				null,
				null,
				 toUserInf.getUserId(),
				 req.getPriBId(),
				null,
				null,
				null);
		//企业信息
		intfaceTransLog.setMchntCode(req.getFromCompanyId());
		intfaceTransLog.setTransDesc(req.getTransDesc());
		intfaceTransLog.setAdditionalInfo(req.getTransList() !=null ?JSONArray.toJSONString(req.getTransList()):"");
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);  //保存接口处交易日志
		//执行操作
		boolean eflag=false;
		try {
			intfaceTransLog.setTransList(req.getTransList()); //多专项账户类型
			eflag=transLogService.execute(intfaceTransLog);
		} catch (AccountBizException accountBizException) {

			return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
		}
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}
	/**
	* @Description: 批量充值
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
		

		
		return null;
		
	}

	/**
	* @Description: 消费接口
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
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		/**获取用户数据*/
		UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				intfaceTransLog,
				req.getDmsRelatedKey(),
				toUserInf.getUserId(),
				req.getTransId(),
				req.getPriBId(),
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
		intfaceTransLog.setAdditionalInfo(JSONObject.toJSONString(req.getTransList())); 
		
		/****保存接口易流水****/
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);
		
		//执行操作
		boolean eflag=false; 
	     try {
			 intfaceTransLog.setTransList(req.getTransList());
			 intfaceTransLog.setAddList(req.getAddList());
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
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			//TODO 重复交易返回
			return ResultsUtil.error("99", "重复交易");
		}
		
		UserInf fromUserInf= null;
		UserInf toUserInf=null; 
		
		//企业账户转账 通过企业Id转账到企业
		if (TransCode.MB40.getCode().equals(req.getTransId())){
			fromUserInf= userInfService.getUserInfByUserName(req.getTfrOutUserId());
			toUserInf =userInfService.getUserInfByUserName(req.getTfrInUserId());

		}else if(TransCode.MB50.getCode().equals(req.getTransId())){
			//企业员工充值 通过手机号转账
			toUserInf=userInfService.getUserInfByMobilePhone(req.getTfrInUserId());
			fromUserInf= userInfService.getUserInfByUserName(req.getTfrOutUserId());
		}
		
		if(fromUserInf==null ){
			return ResultsUtil.error("99", "账户信息不存在{%s}"+req.getTfrOutUserId());
		}
		if(toUserInf==null ){
			return ResultsUtil.error("99", "账户信息不存在{%s}"+req.getTfrInUserId());
		}
		
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				intfaceTransLog,
				req.getDmsRelatedKey(),
				fromUserInf.getUserId(),
				req.getTransId(), 
				req.getTfrOutBId(),
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
												toUserInf.getUserId(),
												req.getTfrInBId(),
												fromUserInf.getUserId(),
												req.getTfrOutBId(),
												null);
	
	intfaceTransLog.setTransDesc(req.getTransDesc());
	intfaceTransLog.setMchntCode(fromUserInf.getCompanyId());
	//保存转账类型信息
	intfaceTransLog.setAdditionalInfo(JSONObject.toJSONString(req));
	intfaceTransLogService.saveOrUpdate(intfaceTransLog);  //保存接口处交易日志
	
	//执行操作
	boolean eflag=false; 
     try {
		 intfaceTransLog.setTransList(req.getTransList());//多专项账户类型
    	 eflag=transLogService.execute(intfaceTransLog); 
      } catch (AccountBizException accountBizException) {  
           return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
      }
	//修改当前接口请求交易状态
	intfaceTransLogService.updateById(intfaceTransLog,eflag);
	return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}

	/**
	 * @Description: 提现
	 * @version: v1.0.0
	 * @author: zhuqi
	 * @date: 2018年12月26日 上午11:11:39
	 * Modification History:
	 * Date         Author          Version
	 *-------------------------------------*
	 * 2018年12月26日     zhuqi           v1.0.0
	 */
	public  BaseResult executeWithDraw(AccountWithDrawReqVo req) throws Exception{
		log.info("==>  提现请求操作 mehtod=executeWithDraw and AccountWithDrawReqVo={}",JSONArray.toJSON(req));
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog !=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error("99", "重复交易");
		}

		UserInf fromUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(),req.getUserChnl());
		if(fromUserInf==null ){
			return ResultsUtil.error("99", "账户信息不存在{%s}"+req.getUserChnlId());
		}

		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				intfaceTransLog,
				req.getDmsRelatedKey(),
				fromUserInf.getUserId(),
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
				 fromUserInf.getUserId(),
				 SpecAccountTypeEnum.A01.getbId(), //从托管账户提现
				 fromUserInf.getUserId(),
				null,
				null);

		intfaceTransLog.setTransDesc(req.getTransDesc());
		intfaceTransLog.setMchntCode(fromUserInf.getCompanyId());
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);  //保存接口处交易日志

		//执行操作
		boolean eflag=false;
		try {
			AccountWithdrawDetail withdrawDetail=new AccountWithdrawDetail();
			withdrawDetail.setReceiverName(req.getReceiverName());
			withdrawDetail.setReceivercardNo(req.getReceiverCardNo());
			withdrawDetail.setReceiverType(req.getReceiverType());
			withdrawDetail.setBankName(req.getBankName());
			withdrawDetail.setBankCode(req.getBankCode());
			withdrawDetail.setBankProvince(req.getBankProvince());
			withdrawDetail.setBankCity(req.getBankCity());
			withdrawDetail.setOrderName(req.getOrderName());
			withdrawDetail.setRemarks(req.getRemarks());
			withdrawDetail.setTransAmount(req.getTransAmt());
			withdrawDetail.setUploadAmount(req.getUploadAmt());
			withdrawDetail.setUserId(fromUserInf.getUserId());
			intfaceTransLog.setWithdrawDetail(withdrawDetail);
			eflag=transLogService.execute(intfaceTransLog);

		} catch (AccountBizException accountBizException) {
			return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
		}
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
	}

	
	/**
	 * 退款操作
	 */
	@Override
	public BaseResult executeRefund(AccountRefundReqVo req) throws Exception {
		
		log.info("==>  退款操作 mehtod=executeRefund and AccountRefundReqVo={}",JSONArray.toJSON(req));
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog !=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error("99", "重复交易");
		}
		intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(null, req.getTransChnl());
		return null;
	}


	/**
	 * 交易信息查询
	 * @param dmsRelatedKey
	 * @return
	 * @throws Exception
	 */
	public BaseResult executeQuery(String dmsRelatedKey,String transChnl) throws Exception{
		log.info("==>  交易信息查询 mehtod=executeQuery and dmsRelatedKey={}",dmsRelatedKey);
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(dmsRelatedKey, transChnl);
		if(intfaceTransLog==null){
			return ResultsUtil.error("99", "交易订单不存在");
		}
		return ResultsUtil.error(StringUtils.isNotEmpty(intfaceTransLog.getRespCode())? intfaceTransLog.getRespCode(): "99","");
	}
}
