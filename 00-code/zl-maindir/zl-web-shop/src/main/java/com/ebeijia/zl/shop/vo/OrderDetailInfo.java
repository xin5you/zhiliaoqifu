package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.dao.goods.domain.Goods;
import com.ebeijia.zl.shop.dao.order.domain.OrderInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class OrderDetailInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    OrderInfo order;

    List<SubOrder> subOrders;
}
