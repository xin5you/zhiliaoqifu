package com.ebeijia.zl.coupon.service;

import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.shop.utils.ShopTransactional;

import java.util.List;

public interface ICouponShareService {

    @ShopTransactional
    List<TbCouponHolder> couponShare(String memberId, String couponCode, Long price, Integer amount);
}
