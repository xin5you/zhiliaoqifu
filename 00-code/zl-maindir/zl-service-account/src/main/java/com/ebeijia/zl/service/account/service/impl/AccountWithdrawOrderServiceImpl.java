package com.ebeijia.zl.service.account.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawOrderMapper;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import org.springframework.stereotype.Service;

/**
 *
 * 账户交易流水 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Service
public class AccountWithdrawOrderServiceImpl extends ServiceImpl<AccountWithdrawOrderMapper, AccountWithdrawOrder> implements IAccountWithdrawOrderService {

    @Override
    public boolean save(AccountWithdrawOrder entity) {
        entity.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setCreateUser("99999999");
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setUpdateUser("99999999");
        entity.setLockVersion(0);
        return super.save(entity);
    }
}
