package com.ebeijia.zl.shop.service.coupon;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;

import java.util.List;

public interface ICouponService {
    int checkVaild(String vaildCode);

    List<TbEcomGoods> listGoods(Integer catid, String order, Integer start, Integer limit);

    TbEcomGoodsDetail goodsDetail(String goodsId);
}
