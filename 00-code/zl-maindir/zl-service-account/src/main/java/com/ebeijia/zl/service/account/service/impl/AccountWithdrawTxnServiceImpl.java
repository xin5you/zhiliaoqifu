package com.ebeijia.zl.service.account.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.req.AccountFrozenReqVo;
import com.ebeijia.zl.facade.account.req.AccountRefundReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.service.account.mapper.AccountWithdrawOrderMapper;
import com.ebeijia.zl.service.account.service.*;
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
public class AccountWithdrawTxnServiceImpl  implements IAccountWithdrawTxnService {

    private Logger logger = LoggerFactory.getLogger(AccountWithdrawTxnServiceImpl.class);

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

    @Autowired
    private ITransLogService transLogService;

    @Autowired
    private IIntfaceTransLogService intfaceTransLogService;

    @Autowired
    private IAccountWithdrawOrderService accountWithdrawOrderService;

    @Autowired
    private IAccountWithdrawDetailService accountWithdrawDetailService;

    @Autowired
    private JedisCluster jedisCluster;


    public boolean doFrozenWithDrawByUser(AccountWithdrawOrder accountWithdrawOrder,String transCode) throws Exception {

        TransLog transLog = transLogService.getById(accountWithdrawOrder.getTxnPrimaryKey());

        if (transLog != null) {
            IntfaceTransLog intfaceTransLog=intfaceTransLogService.getById(transLog.getItfPrimaryKey());
            AccountFrozenReqVo reqVo=new AccountFrozenReqVo();
            String dmsRelateKey=IdUtil.getNextId();
            reqVo.setTransId(transCode);
            reqVo.setTransChnl(TransChnl.CHANNEL40011002.toString());
            reqVo.setUserId(intfaceTransLog.getUserId());
            reqVo.setUserType(UserType.TYPE100.getCode());
            reqVo.setDmsRelatedKey(dmsRelateKey);
            reqVo.setTransAmt(accountWithdrawOrder.getTotalAmount());
            reqVo.setUploadAmt(accountWithdrawOrder.getTotalAmount());
            reqVo.setPriBId(transLog.getPriBId());
            reqVo.setOrgItfPrimaryKey(intfaceTransLog.getItfPrimaryKey());
            BaseResult result = accountTransactionFacade.executeUnFrozen(reqVo);
            if(result !=null && "00".equals(result.getCode())){
                accountWithdrawOrder.setTxnStatus("40");
                accountWithdrawOrderService.updateById(accountWithdrawOrder);
            }else{
                logger.info("#平台请求解冻{}失败：{}",transCode,JSONArray.toJSONString(result));
                throw new RuntimeException();
            }
        }
        return true;
    }

    /**
     *  解冻 请求
     * @param accountWithdrawOrder
     * @param  detail
     * @param  transCode
     * @return
     */
    public boolean doFrozenWithDrawDetailByUser(AccountWithdrawOrder accountWithdrawOrder, AccountWithdrawDetail detail, String transCode) throws Exception{
        TransLog transLog = transLogService.getById(accountWithdrawOrder.getTxnPrimaryKey());

        if (transLog != null) {
            IntfaceTransLog intfaceTransLog=intfaceTransLogService.getById(transLog.getItfPrimaryKey());
            AccountFrozenReqVo reqVo=new AccountFrozenReqVo();
            String dmsRelateKey=IdUtil.getNextId();
            reqVo.setTransId(transCode);
            reqVo.setTransChnl(TransChnl.CHANNEL40011002.toString());
            reqVo.setUserId(intfaceTransLog.getUserId());
            reqVo.setUserType(UserType.TYPE100.getCode());
            reqVo.setDmsRelatedKey(dmsRelateKey);
            reqVo.setTransAmt(detail.getTransAmount());
            reqVo.setUploadAmt(detail.getTransAmount());
            reqVo.setPriBId(transLog.getPriBId());
            reqVo.setOrgItfPrimaryKey(intfaceTransLog.getItfPrimaryKey());
            BaseResult result = accountTransactionFacade.executeUnFrozen(reqVo);
            logger.info("#平台请求解冻扣款返回数据:",JSONArray.toJSONString(result));
            if(result !=null && "00".equals(result.getCode())){
                accountWithdrawOrder.setTxnStatus("40");
                accountWithdrawOrderService.updateById(accountWithdrawOrder);

                detail.setRefPrimaryKey(String.valueOf(result.getObject()));
                accountWithdrawDetailService.updateById(detail);
            }else{
                logger.info("#平台请求解冻{}失败：{}",transCode,JSONArray.toJSONString(result));
                throw new RuntimeException();
            }
        }
        return true;
    }

}
