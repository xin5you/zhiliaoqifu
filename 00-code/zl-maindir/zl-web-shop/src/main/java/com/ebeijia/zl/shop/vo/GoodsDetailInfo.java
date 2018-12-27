package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.dao.goods.domain.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GoodsDetailInfo {
    Long minPrice;
    Long maxPrice;
    TbEcomGoods info;
    TbEcomGoodsDetail detail;

    Map<TbEcomSpecification, Map<TbEcomSpecValues,List<String>>> specsMap;

    Map<String, TbEcomGoodsProduct> products;

}
