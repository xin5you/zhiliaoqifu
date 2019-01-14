package com.ebeijia.zl.coupon.dao.service.impl;

import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.ebeijia.zl.coupon.dao.mapper.TbCouponProductMapper;
import com.ebeijia.zl.coupon.dao.service.ITbCouponProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 卡密产品为卡密的大分类 Service 实现类
 *
 * @User J
 * @Date 2019-01-05
 */
@Service
public class TbCouponProductService extends ServiceImpl<TbCouponProductMapper, TbCouponProduct> implements ITbCouponProductService{

    @Autowired
    private TbCouponProductMapper couponProductMapper;

    @Override
    public List<TbCouponProduct> getCouponList(TbCouponProduct couponProduct) {
        return couponProductMapper.getCouponList(couponProduct);
    }
}
