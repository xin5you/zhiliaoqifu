package com.ebeijia.zl.shop.dao.order.service;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderProductItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * 订单SKU明细表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomOrderProductItemService extends IService<TbEcomOrderProductItem> {

    /**
     * 根据二级订单ID查询货品信息
     * @param sOrderId
     * @return
     */
    TbEcomOrderProductItem getOrderProductItemBySOrderId(String sOrderId);
}
