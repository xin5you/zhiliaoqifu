package com.ebeijia.zl.service.account.facade;

import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.enums.WithDrawSuccessEnum;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawTxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
@Configuration
@com.alibaba.dubbo.config.annotation.Service()
public class AccountWithDrawOrderFacadeImpl implements AccountWithDrawOrderFacade {

	@Autowired
	private IAccountWithdrawOrderService accountWithdrawOrderService;

	@Autowired
	private IAccountWithdrawDetailService accountWithdrawDetailService;

	@Autowired
	private IAccountWithdrawTxnService accountWithdrawTxnService;

	/**
     * 账户提现订单查询
	 * @param bachNo
     * @return
     */
	public AccountWithdrawOrder getAccountWithdrawOrderById(String bachNo){
		return  accountWithdrawOrderService.getById(bachNo);
	}

	/**
     * 账户提现明细查询
	 * @param serialNo
     * @return
     */
	public AccountWithdrawDetail getAccountWithdrawDetailById(String serialNo){
		return accountWithdrawDetailService.getById(serialNo);
	}

	/**
	 * 账户提现订单修改
	 * @param order
	 * @param detail
	 * @return
	 */
	public boolean updateAccountWithdrawOrder(AccountWithdrawOrder order,AccountWithdrawDetail detail) throws Exception{
		String transCode=TransCode.CW93.getCode();
		if(WithDrawSuccessEnum.True.getCode().equals(detail.getSuccess())){
			transCode=TransCode.CW92.getCode();
		}
		boolean eflag=accountWithdrawTxnService.doFrozenWithDrawDetailByUser(order,detail,transCode);
		return eflag;
	}
}
