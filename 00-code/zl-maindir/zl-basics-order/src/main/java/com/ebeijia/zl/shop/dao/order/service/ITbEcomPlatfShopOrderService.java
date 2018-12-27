package com.ebeijia.zl.shop.dao.order.service;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 渠道子订单表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomPlatfShopOrderService extends IService<TbEcomPlatfShopOrder> {

    List<TbEcomPlatfShopOrder> getPlatfShopOrderListByPlatfOrder(TbEcomPlatfShopOrder platfShopOrder);

    List<TbEcomPlatfShopOrder> getPlatfShopOrderList(TbEcomPlatfShopOrder platfShopOrder);

}
