package com.ebeijia.zl.shop.service.order;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.vo.AddressInfo;

public interface IOrderService {
    TbEcomPlatfShopOrder createSimpleOrder(String productId, Integer amounts, AddressInfo address);
}
