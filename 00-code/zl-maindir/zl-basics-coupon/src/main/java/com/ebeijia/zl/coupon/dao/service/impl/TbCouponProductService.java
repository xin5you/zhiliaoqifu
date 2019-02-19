package com.ebeijia.zl.coupon.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
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

    @Override
    public List<TbCouponProduct> getCouponList(TbCouponProduct couponProduct) {
        QueryWrapper<TbCouponProduct> queryWrapper = new QueryWrapper<TbCouponProduct>();
        if (!StringUtil.isNullOrEmpty(couponProduct.getCouponName())) {
            queryWrapper.like("coupon_name", couponProduct.getCouponName());
        }
        if (!StringUtil.isNullOrEmpty(couponProduct.getBId())) {
            queryWrapper.like("b_id", couponProduct.getBId());
        }
        queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
        queryWrapper.orderByAsc("tag_amount");
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }
}
