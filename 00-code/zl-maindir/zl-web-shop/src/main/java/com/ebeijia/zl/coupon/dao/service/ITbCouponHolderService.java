package com.ebeijia.zl.coupon.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;

import java.util.List;

/**
 *
 * 记录用户持有的代金券商品 Service 接口类
 *
 * @User J
 * @Date 2019-01-05
 */
public interface ITbCouponHolderService extends IService<TbCouponHolder> {

   List<TbCouponHolder> listCouponHolder(TbCouponHolder holder);

  TbCouponHolder getCouponHolder(TbCouponHolder holder);
}
