package com.ebeijia.zl.web.oms.inaccount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.basics.billingtype.domain.BillingType;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *
 * tb_inaccount_order Service 接口类
 *
 * @User myGen
 * @Date 2018-12-19
 */
public interface InaccountOrderService extends IService<InaccountOrder> {

    List<InaccountOrder> getInaccountOrderByOrder(InaccountOrder inaccountOrder);

    public PageInfo<InaccountOrder> getInaccountOrderByOrderPage(int startNum, int pageSize, InaccountOrder inaccountOrder);
}
