package com.ebeijia.zl.service.account.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;

/**
 *
 * 账户交易流水 Service接口类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
public interface IAccountWithdrawTxnService  {

    /**
     *  提现扣款  知了平台转供应商账户
     * @param accountWithdrawOrder
     * @return
     */
    boolean doWithdrowForMchtA01(AccountWithdrawOrder accountWithdrawOrder) throws Exception;


    /**
     *  提现退款  商户按批次全额退款
     * @param accountWithdrawOrder
     * @return
     */
    boolean doRefundAllForMchtA01ByZl(AccountWithdrawOrder accountWithdrawOrder) throws Exception;
}
