package com.ebeijia.zl.service.account.facade;

import com.ebeijia.zl.common.utils.enums.TransCode;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.enums.WithDrawSuccessEnum;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawTxnService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
@Configuration
@com.alibaba.dubbo.config.annotation.Service()
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
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

	@Override
	public List<AccountWithdrawOrder> getWithdrawOrderList(AccountWithdrawOrder withdrawOrder) {
		return accountWithdrawOrderService.getWithdrawOrderList(withdrawOrder);
	}

	@Override
	public List<AccountWithdrawDetail> getWithdrawDetailList(AccountWithdrawDetail withdrawDetail) {
		return accountWithdrawDetailService.getWithdrawDetailList(withdrawDetail);
	}

	@Override
	public PageInfo<AccountWithdrawOrder> getWithdrawOrderPage(int startNum, int pageSize, AccountWithdrawOrder withdrawOrder) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<AccountWithdrawOrder> list = getWithdrawOrderList(withdrawOrder);
		if (list != null && list.size() > 0) {
			for (AccountWithdrawOrder o : list) {
				o.setTotalAmount(new BigDecimal(NumberUtils.RMBCentToYuan(o.getTotalAmount().toString())));
			}
		}
		PageInfo<AccountWithdrawOrder> page = new PageInfo<AccountWithdrawOrder>(list);
		return page;
	}

	@Override
	public PageInfo<AccountWithdrawDetail> getWithdrawDetailPage(int startNum, int pageSize, AccountWithdrawDetail withdrawDetail) throws Exception {
		PageHelper.startPage(startNum, pageSize);
		List<AccountWithdrawDetail> list = getWithdrawDetailList(withdrawDetail);
		if (list != null && list.size() > 0) {
			for (AccountWithdrawDetail o : list) {
				o.setTransAmount(new BigDecimal(NumberUtils.RMBCentToYuan(o.getTransAmount().toString())));
				o.setUploadAmount(new BigDecimal(NumberUtils.RMBCentToYuan(o.getUploadAmount().toString())));
			}
		}
		PageInfo<AccountWithdrawDetail> page = new PageInfo<AccountWithdrawDetail>(list);
		return page;
	}
}
