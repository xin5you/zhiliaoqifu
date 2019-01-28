package com.ebeijia.zl.service.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.core.domain.BillingType;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.core.rocketmq.service.MQProducerService;
import com.ebeijia.zl.facade.account.dto.AccountLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.AccountCardAttrEnum;
import com.ebeijia.zl.common.utils.enums.AccountStatusEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.AmountUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.facade.account.dto.AccountInf;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.account.mapper.AccountInfMapper;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import com.ebeijia.zl.service.account.service.IAccountLogService;
import com.ebeijia.zl.service.account.utils.CodeEncryUtils;
import com.ebeijia.zl.service.user.mapper.UserInfMapper;
import redis.clients.jedis.JedisCluster;

/**
 *
 * 用户账户信息 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class AccountInfServiceImpl extends ServiceImpl<AccountInfMapper, AccountInf> implements IAccountInfService{

	private  Logger log = LoggerFactory.getLogger(AccountInfServiceImpl.class);
	
	@Autowired
	private AccountInfMapper accountInfMapper;
	
	@Autowired
	private UserInfMapper userInfMapper;
	
	@Autowired
	private IAccountLogService accountLogService;

	@Autowired
	private JedisCluster jedisCluster;

	@Autowired
	private MQProducerService mqProducerService;
	
	/***
	 * 
	* @Description: 查找賬戶信息
	*
	* @param:userType 用戶类型
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月4日 下午12:34:27 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月4日     zhuqi           v1.0.0
	 */
	public AccountInf getAccountInfByUserType(String userType,String userId,String bId,String companyId){
		if(UserType.TYPE100.getCode().equals(userType)){
			return this.getAccountInfByUserId(userId, bId);
		}else{
			QueryWrapper<UserInf> queryWrapper = new QueryWrapper<UserInf>();
			queryWrapper.eq("company_id", companyId);
			queryWrapper.eq("user_type", userType);
			queryWrapper.eq("data_stat",DataStatEnum.TRUE_STATUS.getCode());
			UserInf userInf= userInfMapper.selectOne(queryWrapper);
			if(userInf==null){
				return null;
			}
			return  this.getAccountInfByUserId(userInf.getUserId(), bId);
		}
	}
	
	/**
	 * 查找用户的专项账户
	 */
	public AccountInf getAccountInfByUserId(String userId,String bId){
		QueryWrapper<AccountInf> queryWrapper = new QueryWrapper<AccountInf>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("b_id", bId);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		return accountInfMapper.selectOne(queryWrapper);
	}
	
	/**
	 * 
	* @Function: IAccountInfService.java
	* @Description: 账户操作
	*
	* @param:voList 交易流水列表
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 下午5:00:18 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean execute(List<TransLog> voList){


		if(voList !=null && voList.size()>0){
			for(TransLog transLog:voList){
				if (AccountCardAttrEnum.OPER.getValue().equals(transLog.getCardAttr())) {
					//账户开户
					this.open(transLog);
				} else if (AccountCardAttrEnum.ADD.getValue().equals(transLog.getCardAttr())) {
					//账户加款
					this.credit(transLog);
				} else if (AccountCardAttrEnum.SUB.getValue().equals(transLog.getCardAttr())) {
					//账户减款
					this.debit(transLog);
				}else if (AccountCardAttrEnum.FROZEN.getValue().equals(transLog.getCardAttr())){
					//账户冻结
					this.Frozen(transLog);
				} else if (AccountCardAttrEnum.COMMINFROZEN.getValue().equals(transLog.getCardAttr())){
					//冻结资金提交
					this.commitFrozen(transLog);
				} else if (AccountCardAttrEnum.UNFROZEN.getValue().equals(transLog.getCardAttr())){
					//账户资金解冻撤销
					this.unFrozen(transLog);
				}else{
					throw AccountBizException.ACCOUNT_CARD_ATTR_ERROR.newInstance("账户不存在,用户编号{%s}", transLog.getCardAttr()).print();
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 *开户操作
	 */
	private boolean open(TransLog transLog) {
		log.info("==>oper transLog={}",JSONArray.toJSON(transLog));
		AccountInf account=this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if(account !=null){
			return true;
		}else{
			account=new AccountInf();
			account.setAccountNo(IdUtil.getNextId());
			account.setUserId(transLog.getUserId());
			account.setAccountType(transLog.getUserType());
			account.setBId(transLog.getPriBId()); //专项账户类型
			account.setAccountStat("00");//00：正常 10：冻结 90：注销
			account.setAccountType(transLog.getUserType());
			account.setAccBal(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_DOWN)); //开户时余额为0
			account.setCouponBal(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_DOWN));
			account.setFreezeAmt(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_DOWN)); //开户冻结金额为0

			boolean flag= this.save(account);
			if(flag){
				flag=accountLogService.save(account, transLog);//保存賬戶信息
			}
			if(!flag){
				throw AccountBizException.ACCOUNT_CREATE_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
			}
			return flag;
		}
	}
	/**
	 * 加款
	 */
	private boolean credit(TransLog transLog) {
		log.info("==>credit transLog={}",JSONArray.toJSON(transLog));
		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}
		//账户退款操作
		if(TransCode.CW11.getCode().equals(transLog.getTransId()) || TransCode.CW74.getCode().equals(transLog.getTransId())){
			AccountLog orgAccountLog=accountLogService.getAccountLogByTxnPriKey(transLog.getOrgTxnPrimaryKey());
			if(orgAccountLog==null){
				throw AccountBizException.ORG_ACCOUNT_LOG_NOT_EXIT.newInstance("原账户交易日志{%s}不存在", transLog.getOrgTxnPrimaryKey()).print();
			}
			//如果原交易金額小于 已退款金额+本次交易金额
			if(AmountUtil.lessThan(orgAccountLog.getTxnAmt(),AmountUtil.add(orgAccountLog.getReturnAmt(),transLog.getTransAmt()))){
				throw AccountBizException.ACCOUNT_REFUND_NOT_ENOUGH.newInstance("退款失败，本次退款大于可退款金额", null).print();
			}

			orgAccountLog.setReturnFlag("1");
			orgAccountLog.setReturnAmt(AmountUtil.add(orgAccountLog.getReturnAmt(),transLog.getTransAmt()));
			boolean f=accountLogService.updateById(orgAccountLog);
		}
		/****** consumerBal set begin ***/
		//员工账户充值 专用专项账户的按比例设置强制消费额度

		if(UserType.TYPE100.getCode().equals(account.getAccountType())){

			// 所有用户的加款采用的最小单位是分！采用平台利益优先规则 小数点直接舍弃
			transLog.setTransAmt(transLog.getTransAmt().setScale(0,BigDecimal.ROUND_DOWN));

			//非 员工通用福利账户 并且 非现金账户
			BillingType billingType=null;
			if(SpecAccountTypeEnum.A00.getCode().equals(account.getBId())){
				//通用账户充值，暂无特定需求
			}else if(SpecAccountTypeEnum.A01.getCode().equals(account.getBId())) {
/*				BigDecimal loseFee=new BigDecimal(0.04); //默认折损率
				billingType=getBillingTypeForCache(account.getBId());

				if(billingType !=null){
					loseFee=billingType.getLoseFee(); //指的是账户转卖代金券的折损率，
				}
				BigDecimal transAmt=AmountUtil.mul(transLog.getTransAmt(),AmountUtil.sub(new BigDecimal(1),loseFee)); //扣除折损率后，到账金额
				if(TransCode.CW90.getCode().equals(transLog.getTransId())){
					transLog.setTransAmt(transAmt);
				}*/
			}else{
				if(TransCode.CW50.getCode().equals(transLog.getTransId())){
					//所有的专用类型的账户充值 都需要按比例划分到消费额度里
					BigDecimal coupon_rate=new BigDecimal(0.9); //默认折扣率
					billingType=getBillingTypeForCache(account.getBId());
					if(billingType !=null){
						coupon_rate=billingType.getBuyFee(); //可购率，指的是购买代金券的比例
					}
					BigDecimal couponBalAmt=AmountUtil.mul(transLog.getTransAmt(),coupon_rate); //加入消费比例是coupon_rate 即可购买的代金券的值
					//账户充值
					account.setCouponBal(AmountUtil.add(account.getCouponBal(), couponBalAmt));
				}
			}
		}
        if(account.getCouponBal()==null){
            account.setCouponBal(new BigDecimal(0));
        }
		/****** consumerBal set end ***/

		/****** 操作余额 ***/
		this.credit(account, transLog.getTransAmt());
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		return true;
	}
	/**
	 * 减款
	 * @return
	 */
	private boolean debit(TransLog transLog) {
		log.info("==>debit transLog={}",JSONArray.toJSON(transLog));

		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}
		
		/****** setCouponBal set begin ***/
		//员工账户消费 扣款
		if(UserType.TYPE100.getCode().equals(account.getAccountType())){

			// 所有用户的扣款采用的最小单位是分！采用平台利益优先规则 小数点直接进位
			transLog.setTransAmt(transLog.getTransAmt().setScale(0,BigDecimal.ROUND_UP));

			//购买代金券
			if(TransCode.CW20.getCode().equals(transLog.getTransId())){
				//如果用户的代金券的额度小于用户的本次交易金额
				if(AmountUtil.lessThan(account.getCouponBal(), transLog.getTransAmt())){
					throw AccountBizException.ACCOUNT_COUPONBAL_IS_NOT_ENOUGH.newInstance("代金券额度不足,用户编号{%s}", transLog.getUserId()).print();
				}else{
					//保存专项类型的代金券额度
					account.setCouponBal(AmountUtil.sub(account.getCouponBal(), transLog.getTransAmt()));
				}
			}
		}
		/****** setCouponBal set end ***/
		
		/****** 操作余额 ***/
		this.debit(account, transLog.getTransAmt(),transLog.getTransId());
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		return true;
	}


	/**
	 * 资金冻结
	 *
	 * @param transLog
	 */
	public boolean Frozen(TransLog transLog) {
		log.info("==>unFrozen transLog={}",JSONArray.toJSON(transLog));
		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}

		/****** 操作余额 ***/
		this.Frozen(account, transLog.getTransAmt(),transLog.getTransId());
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}else {
				//托管账户提现
				if (TransCode.CW91.getCode().equals(transLog.getTransId()) || TransCode.MB90.getCode().equals(transLog.getTransId())) {
					try {
						mqProducerService.sendWithDrawBatchNo(transLog.getBatchNo());
					}catch (Exception ex){
						log.error("托管账户提现发送消息异常，{}",ex);
					}
				}
		}
		return true;
	}


	/**
	 * 冻结提交
	 *
	 * @param transLog
	 */
	public boolean commitFrozen(TransLog transLog) {
		log.info("==>commitFrozen transLog={}",JSONArray.toJSON(transLog));
		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}


		AccountLog orgAccountLog=accountLogService.getAccountLogByTxnPriKey(transLog.getOrgTxnPrimaryKey());
		if(orgAccountLog==null){
			throw AccountBizException.ORG_ACCOUNT_LOG_NOT_EXIT.newInstance("原账户交易日志{%s}不存在", transLog.getOrgTxnPrimaryKey()).print();
		}
		//如果原交易金額小于 冻结提交金额
		if(AmountUtil.lessThan(orgAccountLog.getTxnAmt(),transLog.getTransAmt())){
			throw AccountBizException.ACCOUNT_REFUND_NOT_ENOUGH.newInstance("退款失败，本次退款大于可退款金额", null).print();
		}

		/****** 操作余额 ***/
		this.commitFrozen(account, transLog.getTransAmt());
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		return true;
	}


	/**
	 * 解冻撤销
	 *
	 * @param transLog
	 */
	public boolean unFrozen(TransLog transLog) {
		log.info("==>unFrozen transLog={}",JSONArray.toJSON(transLog));
		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}

		AccountLog orgAccountLog=accountLogService.getAccountLogByTxnPriKey(transLog.getOrgTxnPrimaryKey());
		if(orgAccountLog==null){
			throw AccountBizException.ORG_ACCOUNT_LOG_NOT_EXIT.newInstance("原账户交易日志{%s}不存在", transLog.getOrgTxnPrimaryKey()).print();
		}
		//如果原交易金額小于 已退款金额+本次交易金额
		if(AmountUtil.lessThan(orgAccountLog.getTxnAmt(),AmountUtil.add(orgAccountLog.getReturnAmt(),transLog.getTransAmt()))){
			throw AccountBizException.ACCOUNT_REFUND_NOT_ENOUGH.newInstance("退款失败，本次退款大于可退款金额", null).print();
		}
		orgAccountLog.setReturnFlag("1");
		orgAccountLog.setReturnAmt(AmountUtil.add(orgAccountLog.getReturnAmt(),transLog.getTransAmt()));
		boolean f=accountLogService.updateById(orgAccountLog);

		/****** 操作余额 ***/
		this.unFrozen(account, transLog.getTransAmt());
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		return true;
	}

	/******************************************************业务数据处理*************************************************************/
	/**
	 * 存入
	 * @param account 账户信息
	 * @param transAmt 交易金额
	 */
	public void credit(AccountInf account,BigDecimal transAmt) {

		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		if(account.getAccBal()==null){
            account.setAccBal(new BigDecimal(0));
        }
		account.setAccBal(AmountUtil.add(account.getAccBal(), transAmt));

		if(UserType.TYPE300.getCode().equals(account.getAccountType())) {
			if (AmountUtil.greaterThanOrEqualTo(account.getAccBal(), new BigDecimal(0))) {
				throw AccountBizException.ACCOUNT_TARGET_MCHNT_NOT_COMP.print();
			}
		}
	}

	/**
	 * 支出
	 * @param account
	 * @param transAmt
	 */
	public void debit(AccountInf account,BigDecimal transAmt,String transId) {
		if (! AccountStatusEnum.ACTIVE.getValue().equals(account.getAccountStat())) {
			throw AccountBizException.ACCOUNT_STATUS_IS_INACTIVE.newInstance("账户状态异常,用户编号{%s},账户状态{%s}", account.getAccountNo(),account.getAccountStat()).print();
		}
		if (!this.availableBalanceIsEnough(account,transAmt,transId)) {
			throw AccountBizException.ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH.print();
		}
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		account.setAccBal(AmountUtil.sub(account.getAccBal(), transAmt));
	}


	/**
	 * 申请冻结
	 * @param account
	 * @param transAmt
	 */
	public void Frozen(AccountInf account,BigDecimal transAmt,String transId) {
		if (! AccountStatusEnum.ACTIVE.getValue().equals(account.getAccountStat())) {
			throw AccountBizException.ACCOUNT_STATUS_IS_INACTIVE.newInstance("账户状态异常,用户编号{%s},账户状态{%s}", account.getAccountNo(),account.getAccountStat()).print();
		}
		if (!this.availableBalanceIsEnough(account,transAmt,transId)) {
			throw AccountBizException.ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH.print();
		}
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		account.setFreezeAmt(AmountUtil.add(account.getFreezeAmt(),transAmt)); //冻结金额
		account.setAccBal(AmountUtil.sub(account.getAccBal(), transAmt));
	}

	/**
	 * 冻结提交
	 * @param account 账户信息
	 * @param transAmt 交易金额
	 */
	public void commitFrozen(AccountInf account,BigDecimal transAmt) {
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		if(account.getAccBal()==null){
			account.setAccBal(new BigDecimal(0));
		}
		account.setFreezeAmt(AmountUtil.sub(account.getFreezeAmt(),transAmt));
	}


	/**
	 * 冻结撤销
	 * @param account 账户信息
	 * @param transAmt 交易金额
	 */
	public void unFrozen(AccountInf account,BigDecimal transAmt) {
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		if(account.getAccBal()==null){
			account.setAccBal(new BigDecimal(0));
		}
		account.setFreezeAmt(AmountUtil.sub(account.getFreezeAmt(),transAmt));
		account.setAccBal(AmountUtil.add(account.getAccBal(), transAmt));
	}

	/**
	 * 验证可用余额是否足够
	 * @param account
	 * @param transAmt 交易金额
	 * @return
	 */
	public boolean availableBalanceIsEnough(AccountInf account,BigDecimal transAmt,String transId) {
		if (AmountUtil.greaterThanOrEqualTo(account.getAccBal(), transAmt)) {
			return true;
		} else {
			//商户账户允许为负
			if(! UserType.TYPE100.getCode().equals(account.getAccountType())){
				return true;
			}
			return false;
		}
	}

	private BillingType getBillingTypeForCache(String bId){
		String billingTypeSting=jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BILLING_TYPE,bId);
		BillingType billingType=JSONObject.parseObject(billingTypeSting,BillingType.class);
		return  billingType;
	}

	@Override
	public boolean save(AccountInf entity) {
		entity.setAccBalCode(CodeEncryUtils.generate(entity.getAccBal().toString(), entity.getAccountNo()));//余额加密
		entity.setFreezeAmt(new BigDecimal(0)); //冻结金额
		entity.setMaxTxnAmt1(new BigDecimal(5000000));//单笔Pos交易限额 
		entity.setMaxDayTxnAmt1(new BigDecimal(15000000)); //每日Pos交易限额
		entity.setDayTotalAmt1(new BigDecimal(0));//当天POS交易額
		entity.setMaxTxnAmt2(new BigDecimal(5000000));//单笔web交易限额
		entity.setMaxDayTxnAmt2(new BigDecimal(15000000)); //每日web交易限额
		entity.setDayTotalAmt2(new BigDecimal(0));//当天web交易額
		entity.setFreezeAmt(new BigDecimal(0));
		entity.setLastTxnDate(DateUtil.COMPAT.getDateText(new Date()));
		entity.setLastTxnTime(DateUtil.getCurrentTimeStr());
		
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(AccountInf entity){
		entity.setAccBalCode(CodeEncryUtils.generate(entity.getAccBal().toString(), entity.getAccountNo()));//余额加密
		entity.setLastTxnDate(DateUtil.COMPAT.getDateText(new Date()));
		entity.setLastTxnTime(DateUtil.getCurrentTimeStr());
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		return super.updateById(entity);
	}

	/**
	 * 
	* @Description: 账户列表查询
	*
	* @param:描述1描述
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月14日 下午2:19:31 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月14日     zhuqi           v1.0.0
	 */
	public List<AccountVO> getAccountInfList(AccountQueryReqVo req){
		 List<AccountVO> list=new ArrayList<>();
		 if(UserType.TYPE100.getCode().equals(req.getUserType())){
			 list=accountInfMapper.getAccountVOToUserList(req.getUserChnlId(),req.getUserChnl(),req.getUserType());
		 }else if(UserType.TYPE200.getCode().equals(req.getUserType()) || UserType.TYPE500.getCode().equals(req.getUserType())){
			 list=accountInfMapper.getAccountVOToCompanyList(req.getUserChnlId(),req.getUserType());
		 }else if(UserType.TYPE300.getCode().equals(req.getUserType())){
			 list=accountInfMapper.getAccountVOToProviderList(req.getUserChnlId(),req.getUserType());
		 }else if(UserType.TYPE400.getCode().equals(req.getUserType())){
			 list=accountInfMapper.getAccountVOToRetailList(req.getUserChnlId(),req.getUserType());
		 }
		list.forEach(s ->{
			//单位分转元
			if(s.getAccBal() !=null) {
				s.setAccBal(AmountUtil.RMBCentToYuan(s.getAccBal()));
			}
			if(s.getCouponBal() !=null) {
				s.setCouponBal(AmountUtil.RMBCentToYuan(s.getCouponBal()));
			}
		});
		 return list;
	}

	/**
	 * 查找账户余额
	 * @param userType
	 * @param userChnlId
	 * @param userChnl
	 * @param bId
	 * @return
	 */
	public BigDecimal getAccountInfAccBalByUser(String userType, String userId,String userChnlId, String userChnl, String bId){
		AccountInf accountInf=this.getAccountInfByUserType(userType,userId,bId,userChnlId);
		if(accountInf !=null){
			return accountInf.getAccBal();
		}
		return new BigDecimal(0);
	}
}
