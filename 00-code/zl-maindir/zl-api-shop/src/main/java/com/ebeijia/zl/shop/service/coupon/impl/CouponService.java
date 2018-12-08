package com.ebeijia.zl.shop.service.coupon.impl;

import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsDetail;
import com.ebeijia.zl.shop.service.coupon.ICouponService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService implements ICouponService {
    @Override
    public int checkVaild(String vaildCode) {
        return 0;
    }

    @Override
    public List<TbEcomGoods> listGoods(Integer catid, String order, Integer start, Integer limit) {
        return null;
    }

    @Override
    public TbEcomGoodsDetail goodsDetail(String goodsId) {
        return null;
    }
}
