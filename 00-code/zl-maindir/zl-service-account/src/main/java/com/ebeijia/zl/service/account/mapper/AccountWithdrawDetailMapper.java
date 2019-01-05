package com.ebeijia.zl.service.account.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 *
 * 账户提现记录明细 Mapper 接口
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Mapper
public interface AccountWithdrawDetailMapper extends BaseMapper<AccountWithdrawDetail> {

    /**
     * 单个账户当月提现次数
     * @param userId
     * @param sDate
     * @param eDate
     * @return
     */
    int getWithdrawTotalToMonthByUserId(@Param("userId") String userId, @Param("sDate")long sDate, @Param("eDate")long eDate);

    /**
     * 单个账户 日期范围内 提现金额
     * @return
     */
    BigDecimal getWithdrawAmtByUserIdAndTime(@Param("userId")String userId, @Param("sDate")long sDate, @Param("eDate")long eDate);

    /**
     * 单个银行卡 日期范围内 提现金额
     * @param cardNo
     * @param sDate
     * @param eDate
     * @return
     */
    BigDecimal getWithdrawAmtByCardAndTime(@Param("cardNo")String cardNo, @Param("sDate")long sDate, @Param("eDate")long eDate);

}
