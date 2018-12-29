package com.ebeijia.zl.shop.service.order;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.OrderItemInfo;
import com.ebeijia.zl.shop.vo.PayInfo;

public interface IOrderService {

    @ShopTransactional
    TbEcomPlatfOrder createSimpleOrder(OrderItemInfo orderItemInfo, AddressInfo address);

    TbEcomPlatfOrder cancelOrder(String orderId);

    TbEcomPlatfOrder applyOrder(AddressInfo address, PayInfo payInfo);
}
