package com.ebeijia.zl.service.account.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.vo.TransTypesLog;


/**
 *
 * 账户类型交易日志表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-08
 */
public interface ITransTypesLogService extends IService<TransTypesLog> {

	/**
	* @Description: 获取TransTypesLog
	*
	* @param:txnPimaryKey 交易类型的交易金额
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
	TransTypesLog getTxnAmountByTxnPimaryKey(String txnPimaryKey);
	
	
	boolean save(String txnPimaryKey,BigDecimal trans_amt);
}
