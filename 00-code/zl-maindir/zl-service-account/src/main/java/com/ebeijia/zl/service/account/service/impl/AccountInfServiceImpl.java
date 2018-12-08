package com.ebeijia.zl.service.account.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.facade.account.vo.AccountInf;
import com.ebeijia.zl.facade.account.vo.TransLog;
import com.ebeijia.zl.facade.user.vo.UserInf;
import com.ebeijia.zl.service.account.mapper.AccountInfMapper;
import com.ebeijia.zl.service.account.service.IAccountInfService;
import com.ebeijia.zl.service.account.service.IAccountLogService;
import com.ebeijia.zl.service.account.utils.CodeEncryUtils;
import com.ebeijia.zl.service.user.mapper.UserInfMapper;

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

	private  final Logger log = LoggerFactory.getLogger(AccountInfServiceImpl.class);
	
	@Autowired
	private AccountInfMapper accountInfMapper;
	
	@Autowired
	private UserInfMapper userInfMapper;
	
	@Autowired
	private IAccountLogService accountLogService;
	
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
			return  this.getAccountInfByUserId(userId, bId);
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
					
				}else{
					throw AccountBizException.ACCOUNT_CARD_ATTR_ERROR.newInstance("账户不存在,用户编号{%s}", transLog.getCardAttr()).print();
				}
//				else if (AccountCardAttrEnum.FROZEN.getValue().equals(transLog.getCardAttr())){
//					//账户冻结
//					
//				} else if (AccountCardAttrEnum.UNFROZEN.getValue().equals(transLog.getCardAttr())){
//					//账户解冻
//				}
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
			log.info("==>oper<==");
			return this.save(account);
		}
	}
	
	/**
	 * 加款
	 * 
	 */
	
	private boolean credit(TransLog transLog) {
		log.info("==>credit transLog={}",JSONArray.toJSON(transLog));
		
		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}
		this.credit(account, transLog.getTransAmt());
		
		//员工账户充值 专用专项账户的按比例设置代金券权益额度
		if(UserType.TYPE100.equals(account.getAccountType())){
			if(SpecAccountTypeEnum.A0.equals(account.getBId()) || SpecAccountTypeEnum.A1.equals(account.getBId())){
				
			}else{
				//所有的专用类型的账户充值 都需要按比例划分到消费额度里
				BigDecimal cus_fee=new BigDecimal(0.1).setScale(4,BigDecimal.ROUND_HALF_DOWN); //加入消费比例是0.1 即 10%必须消费
				
			}
		}else{
			//非员工账户，消费额度是0
			account.setConsumerBal(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_DOWN));
		}
		
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		
		log.info("==>credit<==");
		return true;

	}

	/**
	 * 减款
	 * 
	 * @return
	 */
	
	private boolean debit(TransLog transLog) {
		log.info("==>debit transLog={}",JSONArray.toJSON(transLog));

		AccountInf account = this.getAccountInfByUserId(transLog.getUserId(), transLog.getPriBId());
		if (account == null) {
			throw AccountBizException.ACCOUNT_NOT_EXIT.newInstance("账户不存在,用户编号{%s}", transLog.getUserId()).print();
		}
		this.debit(account, transLog.getTransAmt());
		
		boolean flag=accountLogService.save(account, transLog);
		if(flag){
			flag=this.updateById(account);//修改当前賬戶信息
		}
		if(!flag){
			throw AccountBizException.ACCOUNT_TRANS_FAILED.newInstance("交易失敗,交易流水號{%s}", transLog.getTxnPrimaryKey()).print();
		}
		log.info("==>debit<==");
		return true;
	}
	
	
    
	/**
	 * 存入
	 * @param account 账户信息
	 * @param transAmt 交易金额
	 */
	public void credit(AccountInf account,BigDecimal transAmt) {
		
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		account.setAccBal(AmountUtil.add(account.getAccBal(), transAmt.setScale(4,BigDecimal.ROUND_HALF_DOWN)));
	}

	/**
	 * 支出
	 * 
	 * @param amount
	 */
	public void debit(AccountInf account,BigDecimal transAmt) {
		if (! AccountStatusEnum.ACTIVE.getValue().equals(account.getAccountStat())) {
			throw AccountBizException.ACCOUNT_STATUS_IS_INACTIVE.newInstance("账户状态异常,用户编号{%s},账户状态{%s}", account.getAccountNo(),account.getAccountStat()).print();
		}

		if (!this.availableBalanceIsEnough(account,transAmt)) {
			throw AccountBizException.ACCOUNT_AVAILABLEBALANCE_IS_NOT_ENOUGH.print();
		}
		if (!CodeEncryUtils.verify(account.getAccBal().toString(), account.getAccountNo(), account.getAccBalCode())) {
			throw AccountBizException.ACCOUNT_AMOUNT_ERROR.print();
		}
		
		account.setAccBal(AmountUtil.sub(account.getAccBal(), transAmt.setScale(4,BigDecimal.ROUND_HALF_UP)));
	}
	
	/**
	 * 验证可用余额是否足够
	 * 
	 * @param amount
	 * @return
	 */
	public boolean availableBalanceIsEnough(AccountInf account,BigDecimal transAmt) {
		if (AmountUtil.greaterThanOrEqualTo(account.getAccBal(), transAmt)) {
			return true;
		} else {
			return false;
		}
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
}
