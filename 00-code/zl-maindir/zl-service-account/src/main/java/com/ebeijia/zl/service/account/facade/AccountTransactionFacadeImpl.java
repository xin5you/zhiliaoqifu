package com.ebeijia.zl.service.account.facade;

import java.math.BigDecimal;
import java.util.List;

import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.tools.AmountUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.req.*;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import com.ebeijia.zl.service.account.utils.ITFRespCode;
import com.ebeijia.zl.service.valid.AccountTransValidUtils;
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
import redis.clients.jedis.JedisCluster;


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
@com.alibaba.dubbo.config.annotation.Service()
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

	@Autowired
	private IAccountWithdrawDetailService accountWithdrawDetailService;

	@Autowired
	private JedisCluster jedisCluster;
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
		BaseResult resp=new  BaseResult();
		if(AccountTransValidUtils.executeRechargeValid(req,resp)){
			resp.setCode(ITFRespCode.CODE1099.getCode());
			return resp;
		}
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1094.getCode(), ITFRespCode.CODE1094.getValue());
		}
		UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
		if(toUserInf==null){
			return ResultsUtil.error(ITFRespCode.CODE1002.getCode(),String.format("当前用户{%s}所属渠渠道{%s}未开户", req.getTransChnl(),req.getUserChnl()));
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
			log.error("账户充值-executeRecharge error:{}",accountBizException);
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
		BaseResult resp=new  BaseResult();
		if(AccountTransValidUtils.executeConsumeValid(req,resp)){
			resp.setCode(ITFRespCode.CODE1099.getCode());
			return resp;
		}

		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1094.getCode(), ITFRespCode.CODE1094.getValue());
		}
		/**获取用户数据*/
		UserInf fromUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(), req.getUserChnl());
		if(fromUserInf==null){
			return ResultsUtil.error(ITFRespCode.CODE1002.getCode(),String.format("当前用户{%s}所属渠渠道{%s}未开户", req.getTransChnl(),req.getUserChnl()));
		}
		//所有的商户收款，商户必须属于管理平台开户的账户
		UserInf toMchntInf= userInfService.getUserInfByExternalId(req.getMchntCode(),UserChnlCode.USERCHNL1001.getCode());
		if(toMchntInf==null){
			return ResultsUtil.error(ITFRespCode.CODE1005.getCode(),String.format("当前商户{%s}不存在", req.getTransChnl(),req.getUserChnl()));
		}
		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				intfaceTransLog,
				req.getDmsRelatedKey(),
				fromUserInf.getUserId(),
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
				                                toMchntInf.getUserId(),
												req.getPriBId(),
												fromUserInf.getUserId(),
												req.getPriBId(),
												null);
		
		//保存消费类型信息 重要数据存储 重要 重要
		intfaceTransLog.setAdditionalInfo(JSONObject.toJSONString(req.getTransList()));
		intfaceTransLog.setMchntCode(req.getMchntCode());
		intfaceTransLog.setShopCode(req.getShopCode());
		/****保存接口易流水****/
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);
		
		//执行操作
		boolean eflag=false; 
	     try {
			 intfaceTransLog.setTransList(req.getTransList());
			 intfaceTransLog.setAddList(req.getAddList());
	    	 eflag=transLogService.execute(intfaceTransLog); 
	      } catch (AccountBizException accountBizException) {
			 log.error("账户消費-executeConsume error:{}",accountBizException);
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

		BaseResult resp=new  BaseResult();
		if(AccountTransValidUtils.executeTransferValid(req,resp)){
			resp.setCode(ITFRespCode.CODE1099.getCode());
			return resp;
		}
		
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog!=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1094.getCode(), ITFRespCode.CODE1094.getValue());
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
			return ResultsUtil.error(ITFRespCode.CODE1002.getCode(), "账户信息不存在{%s}"+req.getTfrOutUserId());
		}
		if(toUserInf==null ){
			return ResultsUtil.error(ITFRespCode.CODE1005.getCode(), "账户信息不存在{%s}"+req.getTfrInUserId());
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
		 log.error("账户转账-executeTransfer error:{}",accountBizException);
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
		log.info("==>  提现操作 mehtod=executeWithDraw and AccountWithDrawReqVo={}",JSONArray.toJSON(req));

		BaseResult resp=new  BaseResult();
		if(AccountTransValidUtils.executeWithDrawValid(req,resp)){
			resp.setCode(ITFRespCode.CODE1099.getCode());
			return resp;
		}

		/**
		 * 订单交易检验
		 */
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog !=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1094.getCode(), ITFRespCode.CODE1094.getValue());
		}

		UserInf fromUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(),req.getUserChnl());
		if(fromUserInf==null ){
			return ResultsUtil.error(ITFRespCode.CODE1002.getCode(), "账户信息不存在{%s}"+req.getUserChnlId());
		}

		/**所有的商户收款，商户必须属于管理平台开户的账户*/
		String mchtCode=jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code);
		UserInf toMchntInf= userInfService.getUserInfByExternalId(mchtCode,UserChnlCode.USERCHNL1001.getCode());
		if(toMchntInf==null){
			return ResultsUtil.error(ITFRespCode.CODE1005.getCode(),String.format("当前商户{%s}不存在", req.getTransChnl(),req.getUserChnl()));
		}

		long sDate=DateUtil.getMonthBeginInMillis();
		long eDate=DateUtil.getMonthEndInMillis();

		//单个账户单笔消费限额
		BigDecimal txnAmt=new BigDecimal(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_DAY_TIME_AMT"));
		if(AmountUtil.greaterThanOrEqualTo(req.getTransAmt(),txnAmt)){
			return ResultsUtil.error(ITFRespCode.CODE1061.getCode(), ITFRespCode.CODE1061.getValue());
		}
		//step2: 当前用户当月提现金额
		 txnAmt=accountWithdrawDetailService.getWithdrawAmtByUserIdAndTime(fromUserInf.getUserId(),sDate,eDate);

		//如果当前用户已经有提现操作
		if(AmountUtil.bigger(txnAmt,new BigDecimal(0))){

			//用户当月提现金额比较
			BigDecimal withDrawCardAmt=new BigDecimal(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_MONTH_TOTAL_AMT"));
			if(AmountUtil.bigger(AmountUtil.add(txnAmt,req.getTransAmt()),withDrawCardAmt)){
				return ResultsUtil.error(ITFRespCode.CODE1068.getCode(), ITFRespCode.CODE1068.getValue());
			}

			//单月提现次数
			int total=accountWithdrawDetailService.getWithdrawTotalToMonthByUserId(fromUserInf.getUserId(),sDate,eDate);
			int withDrawTotal=Integer.parseInt(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_MONTH_TOTAL_NUM"));
			if(total>=withDrawTotal){
				return ResultsUtil.error(ITFRespCode.CODE1065.getCode(), ITFRespCode.CODE1065.getValue());
			}

			//单日提现金额
			sDate=DateUtil.getStartTimeInMillis();
			eDate=DateUtil.getEndTimeInMillis();
			txnAmt=accountWithdrawDetailService.getWithdrawAmtByUserIdAndTime(fromUserInf.getUserId(),sDate,eDate);
			BigDecimal withDrawDayAmt=new BigDecimal(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_DAY_TOTAL_AMT"));
			if(AmountUtil.bigger(AmountUtil.add(txnAmt,req.getTransAmt()),withDrawDayAmt)){
				return ResultsUtil.error(ITFRespCode.CODE1067.getCode(), ITFRespCode.CODE1067.getValue());
			}
		}


		//step2: 银行卡当月提现金额
		//单张银行卡号当月提现金额
		sDate=DateUtil.getMonthBeginInMillis();
		eDate=DateUtil.getMonthEndInMillis();
		txnAmt=accountWithdrawDetailService.getWithdrawAmtByCardAndTime(req.getReceiverCardNo(),sDate,eDate);
		if(AmountUtil.bigger(txnAmt,new BigDecimal(0))){
			BigDecimal withDrawCardAmt=new BigDecimal(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_CARD_MONTH_TOTAL_AMT"));
			if(AmountUtil.bigger(AmountUtil.add(txnAmt,req.getTransAmt()),withDrawCardAmt)){
				return ResultsUtil.error(ITFRespCode.CODE1069.getCode(), ITFRespCode.CODE1069.getValue());
			}
			//银行卡单日提现金额
			sDate=DateUtil.getStartTimeInMillis();
			eDate=DateUtil.getEndTimeInMillis();
			txnAmt=accountWithdrawDetailService.getWithdrawAmtByCardAndTime(req.getReceiverCardNo(),sDate,eDate);

			withDrawCardAmt=new BigDecimal(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,"WITHDRAW_CARD_DAY_TOTAL_AMT"));
			if(AmountUtil.bigger(AmountUtil.add(txnAmt,req.getTransAmt()),withDrawCardAmt)){
				return ResultsUtil.error(ITFRespCode.CODE1069.getCode(), ITFRespCode.CODE1069.getValue());
			}
		}

		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(
				intfaceTransLog,
				req.getDmsRelatedKey(),
				fromUserInf.getUserId(),
				req.getTransId(),
				SpecAccountTypeEnum.A01.getbId(),
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
				 toMchntInf.getUserId(),
				 SpecAccountTypeEnum.A01.getbId(), //从托管账户提现
				 fromUserInf.getUserId(),
				 SpecAccountTypeEnum.A01.getbId(),
				null);

		intfaceTransLog.setTransDesc(req.getTransDesc());
		intfaceTransLog.setMchntCode(req.getMchntCode());
		intfaceTransLogService.save(intfaceTransLog);  //保存接口处交易日志

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
			log.error("提现操作-executeWithDraw error:{}",accountBizException);
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

		BaseResult resp=new  BaseResult();
		if(AccountTransValidUtils.executeRefundVaild(req,resp)){
			resp.setCode(ITFRespCode.CODE1099.getCode());
			return resp;
		}
		/**
		 * 订单交易检验
		 */
		IntfaceTransLog orgIntfaceTransLog=intfaceTransLogService.getById(req.getOrgItfPrimaryKey());

		if(orgIntfaceTransLog ==null || ! "00".equals(orgIntfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1025.getCode(), ITFRespCode.CODE1025.getValue());
		}
		IntfaceTransLog intfaceTransLog=intfaceTransLogService.getItfTransLogDmsChannelTransId(req.getDmsRelatedKey(), req.getTransChnl());
		if(intfaceTransLog !=null && "00".equals(intfaceTransLog.getRespCode())){
			return ResultsUtil.error(ITFRespCode.CODE1094.getCode(), ITFRespCode.CODE1094.getValue());
		}
		UserInf toUserInf= userInfService.getUserInfByExternalId(req.getUserChnlId(),req.getUserChnl());
		if(toUserInf==null ){
			return ResultsUtil.error(ITFRespCode.CODE1002.getCode(), "账户信息不存在{%s}"+req.getUserChnlId());
		}
		if(!toUserInf.getUserId().equals(orgIntfaceTransLog.getUserId())){
			return ResultsUtil.error(ITFRespCode.CODE1017.getCode(), "退款用户与原交易用户不一致{%s}"+req.getUserChnlId());
		}

		/****实例化接口流水****/
		intfaceTransLog=intfaceTransLogService.newItfTransLog(intfaceTransLog,req.getDmsRelatedKey(), toUserInf.getUserId(), req.getTransId(),null,
				req.getUserType(), req.getTransChnl(),req.getUserChnl(),req.getUserChnlId(),req.getOrgItfPrimaryKey());
		intfaceTransLogService.addBizItfTransLog(
				intfaceTransLog,
				orgIntfaceTransLog.getTransAmt(),
				orgIntfaceTransLog.getUploadAmt(),
				null,
				null,
				null,
				orgIntfaceTransLog.getTfrOutUserId(),  //退款，退回到原来支出的userId
				orgIntfaceTransLog.getTfrOutBId(), //从托管账户提现
				orgIntfaceTransLog.getTfrInUserId(), //退款，原来收款的商户对应userId
				orgIntfaceTransLog.getTfrInBId(),
				null);
		//企业信息
		intfaceTransLog.setTransDesc(req.getTransDesc());
		intfaceTransLog.setAdditionalInfo(req.getTransList() !=null ?JSONArray.toJSONString(req.getTransList()):"");
		intfaceTransLogService.saveOrUpdate(intfaceTransLog);  //保存接口处交易日志

		//执行操作
		boolean eflag=false;
		try {
			intfaceTransLog.setTransList(req.getTransList());//多专项账户类型
			eflag=transLogService.execute(intfaceTransLog);
		} catch (AccountBizException accountBizException) {
			log.error("退款操作-executeRefund error:{}",accountBizException);
			return ResultsUtil.error(String.valueOf(accountBizException.getCode()), accountBizException.getMsg());
		}
		//修改当前接口请求交易状态
		intfaceTransLogService.updateById(intfaceTransLog,eflag);
		return new BaseResult<>(intfaceTransLog.getRespCode(),null,intfaceTransLog.getItfPrimaryKey());
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
			return ResultsUtil.error(ITFRespCode.CODE1025.getCode(), ITFRespCode.CODE1025.getCode());
		}
		return ResultsUtil.error(StringUtils.isNotEmpty(intfaceTransLog.getRespCode())? intfaceTransLog.getRespCode(): ITFRespCode.CODE1099.getCode(),"");
	}
}
