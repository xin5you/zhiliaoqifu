package com.ebeijia.zl.shop.dao.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.shop.dao.order.domain.OrderInfo;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;

import java.util.List;

/**
 *
 * 订单总表 Service 接口类
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
public interface ITbEcomPlatfOrderService extends IService<TbEcomPlatfOrder> {

    OrderInfo getOrderInfo(OrderInfo orderInfo);

    List<TbEcomPlatfOrder> getPlatfOrderList(TbEcomPlatfOrder platfOrder);

}
