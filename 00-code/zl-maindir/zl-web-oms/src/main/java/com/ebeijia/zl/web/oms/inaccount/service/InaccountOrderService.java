package com.ebeijia.zl.web.oms.inaccount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    InaccountOrder getInaccountOrderByOrderId(InaccountOrder inaccountOrderv);

    InaccountOrder getInaccountOrderByOrderIdAndCompanyId(InaccountOrder inaccountOrder);

}
