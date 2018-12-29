package com.ebeijia.zl.service.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.exceptions.AccountBizException;

import java.util.List;


/**
 *
 * 账户交易流水 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
public interface ITransLogService extends IService<TransLog> {

	/**
	* 
	* @Function: ITransLogService.java
	* @Description: 創建賬戶交易流水
	*
	* @param:intfaceTransLog 接口層流水
	*
	* @version: v1.0.0
	* @author: zhuqi
	* @date: 2018年12月3日 上午11:35:33 
	*
	* Modification History:
	* Date         Author          Version
	*-------------------------------------*
	* 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean execute(IntfaceTransLog intfaceTransLog) throws AccountBizException;

	/**
	 *
	 * @Function: ITransLogService.java
	 * @Description: 創建賬戶交易流水
	 *
	 * @param:intfaceTransLogs 接口層流水集合
	 *
	 * @version: v1.0.0
	 * @author: zhuqi
	 * @date: 2018年12月3日 上午11:35:33
	 *
	 * Modification History:
	 * Date         Author          Version
	 *-------------------------------------*
	 * 2018年12月3日     zhuqi           v1.0.0
	 */
	public boolean executeList(List<IntfaceTransLog> intfaceTransLogs) throws AccountBizException;
}
