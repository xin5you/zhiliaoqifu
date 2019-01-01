package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.dao.goods.domain.Goods;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SubOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    TbEcomPlatfShopOrder shopOrder;
    //这里需要把Goods名字和价格替换为orderItem记录的值
    List<Goods> products;
    //SKU和数量的键值对
    Map<String,Integer> amount;
}
