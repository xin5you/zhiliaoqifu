package com.ebeijia.zl.shop.service.order;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.OrderDetailInfo;
import com.ebeijia.zl.shop.vo.OrderItemInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;

public interface IOrderService {

    @ShopTransactional
    TbEcomPlatfOrder createSimpleOrder(OrderItemInfo orderItemInfo, AddressInfo address);

    @ShopTransactional
    TbEcomPlatfOrder cancelOrder(String orderId);

    @ShopTransactional
    TbEcomPlatfOrder applyOrder(PayInfo payInfo);

    OrderDetailInfo orderDetail(String orderId);

    PageInfo<OrderDetailInfo> listOrderDetail(String orderStat, Integer start, Integer limit);
}
