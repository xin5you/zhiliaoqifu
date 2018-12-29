package com.ebeijia.zl.web.api.model.withdraw.service;

import com.ebeijia.zl.core.withdraw.suning.vo.Content;

public interface BizAccountWithdrawOrderService {

    boolean YFBBatchWithdrawNotify(Content content) throws Exception;
}
