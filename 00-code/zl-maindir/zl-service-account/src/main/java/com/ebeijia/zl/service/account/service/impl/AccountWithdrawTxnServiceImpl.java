package com.ebeijia.zl.service.account.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.core.redis.constants.RedisDictKey;
import com.ebeijia.zl.core.redis.utils.RedisConstants;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.dto.IntfaceTransLog;
import com.ebeijia.zl.facade.account.dto.TransLog;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
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
    private JedisCluster jedisCluster;


    /**
     *  调用供应商商接口，提现扣款
     * @param accountWithdrawOrder
     * @return
     */
    public boolean doWithdrowForMchtA01(AccountWithdrawOrder accountWithdrawOrder) throws Exception{
        AccountConsumeReqVo req=new AccountConsumeReqVo();
        req.setTransId(TransCode.MB10.getCode());
        req.setTransChnl(TransChnl.CHANNEL40011002.toString());
        req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
        req.setUserChnlId(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code));
        req.setUserType(UserType.TYPE200.getCode());
        req.setTransAmt(accountWithdrawOrder.getTotalAmount());
        req.setUploadAmt(accountWithdrawOrder.getTotalAmount());
        req.setDmsRelatedKey(accountWithdrawOrder.getBatchNo());
        req.setPriBId(SpecAccountTypeEnum.A01.getbId());
        req.setTransDesc("转出到卡扣款");
        req.setTransNumber(1);
        req.setMchntCode(accountWithdrawOrder.getProviderId()); //处理的商户
        BaseResult result=accountTransactionFacade.executeConsume(req);
        if(result !=null && "00".equals(result.getCode())){
            accountWithdrawOrder.setTxnStatus("20");
            accountWithdrawOrder.setItfPayKey1(String.valueOf(result.getObject()));
            accountWithdrawOrderService.updateById(accountWithdrawOrder);
            return true;
        }else{
            logger.info("#平台请求代付机构扣款失败：{}",JSONArray.toJSONString(result));
            throw new RuntimeException();
        }
    }


    public boolean doRefundAllForMchtA01ByZl(AccountWithdrawOrder accountWithdrawOrder) throws Exception{
        AccountRefundReqVo req=new AccountRefundReqVo();
        req.setTransId(TransCode.CW11.getCode());
        req.setTransChnl(TransChnl.CHANNEL40011002.toString());
        req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
        req.setUserChnlId(jedisCluster.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,RedisDictKey.zlqf_mchnt_code));
        req.setUserType(UserType.TYPE200.getCode());
        req.setOrgItfPrimaryKey(accountWithdrawOrder.getItfPayKey1());
        req.setDmsRelatedKey(IdUtil.getNextId());

        AccountTxnVo vo=new AccountTxnVo();
        vo.setTxnBId(SpecAccountTypeEnum.A01.getbId());
        vo.setTxnAmt(accountWithdrawOrder.getTotalAmount());
        vo.setUpLoadAmt(accountWithdrawOrder.getTotalAmount());
        List translist=new ArrayList();
        translist.add(vo);

        req.setTransList(translist);
        req.setTransDesc("转出到卡失败退款");
        BaseResult result=accountTransactionFacade.executeRefund(req);

        if(result !=null && "00".equals(result.getCode())){
            accountWithdrawOrder.setTxnStatus("30");
            accountWithdrawOrderService.updateById(accountWithdrawOrder);
            try {
                this.doRefundAllForMchtA01ByUser(accountWithdrawOrder);
            }catch (Exception ex){
                logger.error("#平台请求代付机构扣款失败：{}",ex);
            }

        }else{
            logger.info("#平台请求代付机构扣款失败：{}",JSONArray.toJSONString(result));
            return false;
        }
        return false;
    }


    public boolean doRefundAllForMchtA01ByUser(AccountWithdrawOrder accountWithdrawOrder) throws Exception {

        TransLog transLog = transLogService.getById(accountWithdrawOrder.getTxnPrimaryKey());

        if (transLog != null) {
            IntfaceTransLog intfaceTransLog=intfaceTransLogService.getById(transLog.getItfPrimaryKey());
            AccountRefundReqVo req = new AccountRefundReqVo();
            req.setTransId(TransCode.CW11.getCode());
            req.setTransChnl(TransChnl.CHANNEL40011002.toString());
            req.setUserChnl(intfaceTransLog.getUserChnl());
            req.setUserChnlId(intfaceTransLog.getUserChnlId());
            req.setUserType(UserType.TYPE100.getCode());
            req.setOrgItfPrimaryKey(transLog.getItfPrimaryKey());
            req.setDmsRelatedKey(IdUtil.getNextId());
            req.setOrgItfPrimaryKey(intfaceTransLog.getItfPrimaryKey());

            AccountTxnVo vo=new AccountTxnVo();
            vo.setTxnBId(SpecAccountTypeEnum.A01.getbId());
            vo.setTxnAmt(accountWithdrawOrder.getTotalAmount());
            vo.setUpLoadAmt(accountWithdrawOrder.getTotalAmount());

            List translist=new ArrayList();
            translist.add(vo);
            req.setTransList(translist);
            req.setTransDesc("转出到卡失败退款");

            BaseResult result=accountTransactionFacade.executeRefund(req);
            if(result !=null && "00".equals(result.getCode())){
                accountWithdrawOrder.setTxnStatus("40");
                accountWithdrawOrderService.updateById(accountWithdrawOrder);
            }else{
                logger.info("#平台请求代付机构扣款失败：{}",JSONArray.toJSONString(result));
                throw new RuntimeException();
            }
        }
        return true;
    }

}
