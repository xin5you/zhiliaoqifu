package com.ebeijia.zl.service.account.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.account.vo.TransTypesLog;
import com.ebeijia.zl.service.account.mapper.TransTypesLogMapper;
import com.ebeijia.zl.service.account.service.ITransTypesLogService;

/**
 *
 * 账户类型交易日志表 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
@Service
public class TransTypesLogServiceImpl extends ServiceImpl<TransTypesLogMapper, TransTypesLog> implements ITransTypesLogService{
	
	
	@Autowired
	private TransTypesLogMapper  transTypesLogMapper;

	/**
	* @Description: 获取TransTypesLog
	*
	* @param:txnPrimaryKey 交易类型的交易金额
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月8日 下午1:39:26 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月8日     zhuqi           v1.0.0
	 */
	public TransTypesLog getTxnAmountByTxnPimaryKey(String txnPimaryKey){
		QueryWrapper<TransTypesLog> queryWrapper = new QueryWrapper<TransTypesLog>();
		queryWrapper.eq("txn_primary_key", txnPimaryKey);
		queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
		return  transTypesLogMapper.selectOne(queryWrapper);
	}
	
	
	public boolean save(String txnPrimaryKey,BigDecimal transAmt){
		TransTypesLog entity=new TransTypesLog();
		entity.setTpPrimaryKey(IdUtil.getNextId());
		entity.setTxnPrimaryKey(txnPrimaryKey);
		entity.setTransAmt(transAmt);
		entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
		entity.setCreateTime(System.currentTimeMillis());
		entity.setCreateUser("99999999");
		entity.setUpdateTime(System.currentTimeMillis());
		entity.setUpdateUser("99999999");
		entity.setLockVersion(0);
		return super.save(entity);
	}
}
