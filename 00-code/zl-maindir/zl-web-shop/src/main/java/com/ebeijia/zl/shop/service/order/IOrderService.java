package com.ebeijia.zl.shop.service.order;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderInf;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.OrderItemInfo;

public interface IOrderService {

    @ShopTransactional
    TbEcomPlatfShopOrder createSimpleOrder(OrderItemInfo orderItemInfo, AddressInfo address);

    TbEcomOrderInf changeOrderState(String orderId, String state);
}
