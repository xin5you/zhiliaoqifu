package com.ebeijia.zl.service.account.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;

import java.util.List;

/**
 *
 * 账户交易流水 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
public interface IAccountWithdrawOrderService extends IService<AccountWithdrawOrder> {

    /**
     * 查询提现订单信息列表
     * @param withdrawOrder
     * @return
     */
    List<AccountWithdrawOrder> getWithdrawOrderList(AccountWithdrawOrder withdrawOrder);


}
