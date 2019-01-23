package com.ebeijia.zl.service.account.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.tools.SnowFlake;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.req.AccountRefundReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawOrderMapper;
import com.ebeijia.zl.service.account.service.IAccountWithdrawDetailService;
import com.ebeijia.zl.service.account.service.IAccountWithdrawOrderService;
import com.ebeijia.zl.service.account.service.ITransLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 账户交易流水 Service 实现类
 *
 * @User zhuqi
 * @Date 2018-12-26
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor=Exception.class)
public class AccountWithdrawOrderServiceImpl extends ServiceImpl<AccountWithdrawOrderMapper, AccountWithdrawOrder> implements IAccountWithdrawOrderService {

    private Logger logger = LoggerFactory.getLogger(AccountWithdrawOrderServiceImpl.class);

    @Autowired
    private IAccountWithdrawDetailService accountWithdrawDetailService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

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
