package com.ebeijia.zl.web.api.model.withdraw.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ebeijia.zl.core.withdraw.suning.vo.Content;
import com.ebeijia.zl.core.withdraw.suning.vo.TransferOrders;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.enums.WithDrawStatusEnum;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import com.ebeijia.zl.web.api.model.withdraw.service.BizAccountWithdrawOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("bizAccountWithdrawOrderService")
public class BizAccountWithdrawOrderServiceImpl implements BizAccountWithdrawOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountWithDrawOrderFacade accountWithDrawOrderFacade;

    @Override
    public boolean YFBBatchWithdrawNotify(Content content) throws Exception {

        String batchNo=content.getBatchNo();
        AccountWithdrawOrder accountWithdrawOrder=accountWithDrawOrderFacade.getAccountWithdrawOrderById(batchNo);

        if(accountWithdrawOrder ==null){
            logger.info("易付宝回调请求当前批次号不存在->{}",batchNo);
            return false;
        }

        if(WithDrawStatusEnum.Status04.equals(accountWithdrawOrder.getStatus())){
            logger.info("重复发送，当前批次号->{}已失败",batchNo);
            return false;
        }

        if(WithDrawStatusEnum.Status05.getCode().equals(accountWithdrawOrder.getStatus()) || WithDrawStatusEnum.Status07.getCode().equals(accountWithdrawOrder.getStatus())){
            logger.info("重复发送，当前批次号->{}已成功",batchNo);
            return false;
        }
        accountWithdrawOrder.setDatasource(content.getDataSource());
        accountWithdrawOrder.setSuccessNum(content.getSuccessNum());
        accountWithdrawOrder.setSuccessAmount(new BigDecimal(content.getSuccessAmount()));
        accountWithdrawOrder.setFailNum(content.getFailNum());
        accountWithdrawOrder.setFailAmount(new BigDecimal(content.getFailAmount()));
        accountWithdrawOrder.setPoundage(new BigDecimal(content.getPoundage()));
        accountWithdrawOrder.setErrorCode(content.getErrorCode());
        accountWithdrawOrder.setErrorMsg(content.getErrorMsg());
        accountWithdrawOrder.setStatus(content.getStatus());

        TransferOrders transferOrders=content.getTransferOrders().get(0);
        AccountWithdrawDetail accountWithdrawDetail=accountWithDrawOrderFacade.getAccountWithdrawDetailById(transferOrders.getSerialNo());

        if(transferOrders.getId() !=null) {
            accountWithdrawDetail.setDmsPayNo(String.valueOf(transferOrders.getId()));
        }
        accountWithdrawDetail.setPayTime(transferOrders.getPayTime());
        if(StringUtils.isNotEmpty(transferOrders.getRefundTicket())) {
            accountWithdrawDetail.setRefundTicket(transferOrders.getRefundTicket());
        }
        accountWithdrawDetail.setSuccess(transferOrders.getSuccess().toString());

        return accountWithDrawOrderFacade.updateAccountWithdrawOrder(accountWithdrawOrder,accountWithdrawDetail);
    }
}
