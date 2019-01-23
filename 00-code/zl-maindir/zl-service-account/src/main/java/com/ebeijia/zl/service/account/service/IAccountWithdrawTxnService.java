package com.ebeijia.zl.service.account.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
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
     *  解冻 请求
     * @param accountWithdrawOrder
     * @param  transCode
     * @return
     */
    boolean doFrozenWithDrawByUser(AccountWithdrawOrder accountWithdrawOrder,String transCode) throws Exception;


    /**
     *  解冻 请求
     * @param accountWithdrawOrder
     * @param  detail
     * @param  transCode
     * @return
     */
    boolean doFrozenWithDrawDetailByUser(AccountWithdrawOrder accountWithdrawOrder, AccountWithdrawDetail detail, String transCode) throws Exception;

}
