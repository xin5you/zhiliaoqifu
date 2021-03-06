package com.ebeijia.zl.facade.account.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.req.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @Description: 提现订单servcie
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月28日 上午10:59:39
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年11月30日     zhuqi           v1.0.0
 */
public interface AccountWithDrawOrderFacade {

	/**
	 * 账户提现订单查询
	 * @param bachNo
	 * @return
	 */
	AccountWithdrawOrder getAccountWithdrawOrderById(String bachNo);

	/**
	 * 账户提现明细查询
	 * @param serialNo
	 * @return
	 */
	AccountWithdrawDetail getAccountWithdrawDetailById(String serialNo);

	/**
	 * 账户提现订单修改
	 * @param order
	 * @param detail
	 * @return
	 */
	boolean updateAccountWithdrawOrder(AccountWithdrawOrder order,AccountWithdrawDetail detail) throws Exception;

	/**
	 * 查询提现订单信息列表
	 * @param withdrawOrder
	 * @return
	 */
	List<AccountWithdrawOrder> getWithdrawOrderList(AccountWithdrawOrder withdrawOrder);

	/**
	 * 查询提现订单明细信息列表
	 * @param withdrawDetail
	 * @return
	 */
	List<AccountWithdrawDetail> getWithdrawDetailList(AccountWithdrawDetail withdrawDetail);

	/**
	 * 查询提现订单信息列表（含分页）
	 * @param startNum
	 * @param pageSize
	 * @param withdrawOrder
	 * @return
	 * @throws Exception
	 */
	PageInfo<AccountWithdrawOrder> getWithdrawOrderPage(int startNum, int pageSize, AccountWithdrawOrder withdrawOrder) throws Exception;

	/**
	 * 查询提现订单信息列表（含分页）
	 * @param startNum
	 * @param pageSize
	 * @param withdrawDetail
	 * @return
	 * @throws Exception
	 */
	PageInfo<AccountWithdrawDetail> getWithdrawDetailPage(int startNum, int pageSize, AccountWithdrawDetail withdrawDetail) throws Exception;

}
