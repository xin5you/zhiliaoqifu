package com.ebeijia.zl.coupon.dao.service.impl;

import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.mapper.TbCouponHolderMapper;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 记录用户持有的代金券商品 Service 实现类
 *
 * @User J
 * @Date 2019-01-05
 */
@Service
public class TbCouponHolderService extends ServiceImpl<TbCouponHolderMapper, TbCouponHolder> implements ITbCouponHolderService{

    @Override
    public List<TbCouponHolder> listCouponHolder(TbCouponHolder holder) {
        return baseMapper.listCouponHolder(holder);
    }

    @Override
    public TbCouponHolder getCouponHolder(TbCouponHolder holder) {
        return baseMapper.getCouponHolder(holder);
    }

}
