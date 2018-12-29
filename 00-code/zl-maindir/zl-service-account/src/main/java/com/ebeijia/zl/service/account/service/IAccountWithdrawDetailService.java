package com.ebeijia.zl.service.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;

import java.util.List;


/**
 *
 * 账户提现记录明细 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
public interface IAccountWithdrawDetailService extends IService<AccountWithdrawDetail> {

    /**
     * 根据批次号获取提现数据
     * @param batchNo
     * @return
     */
    List<AccountWithdrawDetail> getListByBatchNo(String batchNo);
}
