package com.ebeijia.zl.service.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.AmountUtil;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.telrecharge.domain.CompanyInf;
import com.ebeijia.zl.facade.telrecharge.service.CompanyInfFacade;
import com.ebeijia.zl.service.account.service.IAccountLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.facade.account.dto.AccountInf;
import com.ebeijia.zl.facade.account.dto.AccountLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;
import com.ebeijia.zl.service.account.mapper.AccountLogMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 账户交易日志 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class AccountLogServiceImpl extends ServiceImpl<AccountLogMapper, AccountLog> implements IAccountLogService {

    @Autowired
    private AccountLogMapper accountLogMapper;

	@Autowired
    private CompanyInfFacade companyInfFacade;

	/**
	 * 
	* @Function: AccountLogServiceImpl.java
	* @Description: 賬戶日誌記錄表
	*
	* @param:entity
	* @param:accountInf
	* @param:transLog
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 上午11:04:49 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean save(AccountInf accountInf,TransLog transLog) throws AccountBizException{
		AccountLog entity=new AccountLog();
		entity.setActPrimaryKey(IdUtil.getNextId());
		entity.setAccountNo(accountInf.getAccountNo()); //賬戶Id
		entity.setTxnPrimaryKey(transLog.getTxnPrimaryKey()); //交易流水號
		entity.setTxnAmt(transLog.getTransAmt());  //交易金額
		entity.setTxnRealAmt(transLog.getTransAmt()); //賬戶處理金額
		entity.setTransChnl(transLog.getTransChnl()); //交易渠道
		entity.setAccType(transLog.getCardAttr());
		entity.setTransId(transLog.getTransId());
		entity.setTxnDate(DateUtil.COMPAT.getDateText(new Date()));
		entity.setTxnTime(DateUtil.getCurrentTimeStr());
		entity.setAccTotalBal(accountInf.getAccBal()); //賬戶總金額
		entity.setCancelFlag("0");
		entity.setRevsalFlag("0");
		entity.setReturnFlag("0");
		entity.setReturnAmt(new BigDecimal(0));
		entity.setAdjustFlag("0");
		entity.setAdjustAmt(new BigDecimal(0));
		entity.setRemarks(transLog.getRemarks());
		
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}

	/**
	 * 账单查询
	 * @param req
	 * @return
	 */
	public List<AccountLogVO> getAccountLogVoList(AccountQueryReqVo req) {
		List<AccountLogVO>  list= accountLogMapper.getAccountLogVoList(req);


		list.forEach(s ->{
			//单位分转元
			if(s.getTxnAmt() !=null) {
				s.setTxnAmt(AmountUtil.RMBCentToYuan(s.getTxnAmt()));
			}
			if(s.getAccTotalBal() !=null) {
				s.setAccTotalBal(AmountUtil.RMBCentToYuan(s.getAccTotalBal()));
			}

			//公司名称
			if (TransCode.CW50.getCode().equals(s.getTransId())
					&& UserType.TYPE100.getCode().equals(s.getUserType())
					&& TransChnl.CHANNEL0.getValue().equals(s.getTransChnl())){
				if(StringUtil.isNotEmpty(s.getMchntCode())) {
					CompanyInf companyInf = companyInfFacade.getCompanyInfById(s.getMchntCode());
					if(companyInf !=null) {
						s.setMchntName(companyInf.getName());
					}
				}
			}
		});
		return  list;
	}

	/**
	 * 查找交易日志对应的账户流水信息
	 * @param txnPriKey
	 * @return
	 */
	public AccountLog getAccountLogByTxnPriKey(String txnPriKey){
		QueryWrapper<AccountLog> queryWrapper = new QueryWrapper<AccountLog>();
		queryWrapper.eq("txn_primary_key", txnPriKey);
		return accountLogMapper.selectOne(queryWrapper);
	}
}
