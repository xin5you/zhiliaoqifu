package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.dao.goods.domain.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class GoodsDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    Long minPrice;
    Long maxPrice;
    Boolean hasStore;
    TbEcomGoods info;
    TbEcomGoodsDetail detail;

    Map<String, Map<String,List<String>>> specsMap;

    Map<String, TbEcomGoodsProduct> products;
    Map<String, TbEcomSpecification> specNames;
    Map<String, TbEcomSpecValues> valueNames;


}
