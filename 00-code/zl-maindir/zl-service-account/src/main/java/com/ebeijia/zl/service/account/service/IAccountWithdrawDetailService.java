package com.ebeijia.zl.service.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;

import java.math.BigDecimal;
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

    /**
     * 单个账户当月提现次数
     * @param userId
     * @param sDate
     * @param eDate
     * @return
     */
    int getWithdrawTotalToMonthByUserId( String userId, long sDate,long eDate);

    /**
     * 单个账户 日期范围内 提现金额
     * @return
     */
    BigDecimal getWithdrawAmtByUserIdAndTime(String userId,long sDate, long eDate);

    /**
     * 单个银行卡 日期范围内 提现金额
     * @param cardNo
     * @param sDate
     * @param eDate
     * @return
     */
    BigDecimal getWithdrawAmtByCardAndTime(String cardNo, long sDate,long eDate);
}
