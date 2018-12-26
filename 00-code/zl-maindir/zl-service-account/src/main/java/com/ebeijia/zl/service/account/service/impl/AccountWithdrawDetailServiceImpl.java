package com.ebeijia.zl.service.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawDetailMapper;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

/**
 *
 * 账户提现记录明细 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Service
public class AccountWithdrawDetailServiceImpl extends ServiceImpl<AccountWithdrawDetailMapper, AccountWithdrawDetail> implements IAccountWithdrawDetailService {

    @Override
    public boolean save(AccountWithdrawDetail entity) {
        entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUser("99999999");
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUser("99999999");
        entity.setLockVersion(0);
        return super.save(entity);
    }
}
