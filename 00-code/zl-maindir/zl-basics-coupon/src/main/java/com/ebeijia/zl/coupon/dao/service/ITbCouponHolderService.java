package com.ebeijia.zl.coupon.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 记录用户持有的代金券商品 Service 接口类
 *
 * @User J
 * @Date 2019-01-05
 */
public interface ITbCouponHolderService extends IService<TbCouponHolder> {

    List<TbCouponHolder> listCouponHolder(TbCouponHolder holder);

    TbCouponHolder getCouponHolder(TbCouponHolder holder);

//    List<TbCouponHolder> couponShare(String memberId, String couponCode, Long price, Integer amount);

    List<TbCouponHolder> couponShareRollback(List<TbCouponHolder> list);

    /**
     * 查询所有卡券订单记录列表
     * @param transStat
     * @return
     */
    List<TbCouponHolder> listCouponHolderByCouponHolder(TbCouponHolder couponHolder);

    /**
     * 查询所有卡券订单记录列表（含分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    PageInfo<TbCouponHolder> getTbCouponHolderPage(int startNum, int pageSize, TbCouponHolder entity) throws Exception;

}
