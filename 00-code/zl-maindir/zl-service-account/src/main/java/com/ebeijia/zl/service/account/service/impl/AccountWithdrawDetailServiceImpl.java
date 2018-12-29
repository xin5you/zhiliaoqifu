package com.ebeijia.zl.service.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.user.vo.PersonInf;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawDetailMapper;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * 账户提现记录明细 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class AccountWithdrawDetailServiceImpl extends ServiceImpl<AccountWithdrawDetailMapper, AccountWithdrawDetail> implements IAccountWithdrawDetailService {

    @Autowired
    private AccountWithdrawDetailMapper accountWithdrawDetailMapper;

   public List<AccountWithdrawDetail> getListByBatchNo(String batchNo){
       QueryWrapper<AccountWithdrawDetail> queryWrapper = new QueryWrapper<AccountWithdrawDetail>();
       queryWrapper.eq("batch_no", batchNo);
       queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
       return accountWithdrawDetailMapper.selectList(queryWrapper);
    }
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

    void test(){

    }
}
