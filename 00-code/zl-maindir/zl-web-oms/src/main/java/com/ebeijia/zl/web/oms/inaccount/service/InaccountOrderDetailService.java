package com.ebeijia.zl.web.oms.inaccount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;

import java.util.List;

/**
 *
 * tb_inaccount_order_detail Service 接口类
 *
 * @User myGen
 * @Date 2018-12-19
 */
public interface InaccountOrderDetailService extends IService<InaccountOrderDetail> {

    List<InaccountOrderDetail> getInaccountOrderDetailByOrderId(String orderId);
}
