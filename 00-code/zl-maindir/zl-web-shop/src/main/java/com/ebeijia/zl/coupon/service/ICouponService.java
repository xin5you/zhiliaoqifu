package com.ebeijia.zl.coupon.service;

import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.github.pagehelper.PageInfo;

public interface ICouponService {


    @ShopTransactional
    int couponShare(String vaildCode, String couponCode, Long price, Integer amount);

    @ShopTransactional
    int buyCoupon(String vaildCode, String couponCode, Integer amount);

    PageInfo<TbCouponProduct> listProduct(String bId, String order, Integer start, Integer limit);

    TbCouponProduct couponDetail(String couponCode);

    PageInfo<TbCouponHolder> getHolder(String bId);


    TbCouponHolder getHolderByCouponCode(String couponCode, Long price);
}
