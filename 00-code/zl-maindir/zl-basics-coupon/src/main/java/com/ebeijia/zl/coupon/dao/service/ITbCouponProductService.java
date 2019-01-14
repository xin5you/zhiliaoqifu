package com.ebeijia.zl.coupon.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;

import java.util.List;

/**
 *
 * 卡密产品为卡密的大分类 Service 接口类
 *
 * @User J
 * @Date 2019-01-05
 */
public interface ITbCouponProductService extends IService<TbCouponProduct> {


    /**
     * 根据条件查询商品规格信息
     * @param couponProduct
     * @return
     */
    List<TbCouponProduct> getCouponList(TbCouponProduct couponProduct);
}
