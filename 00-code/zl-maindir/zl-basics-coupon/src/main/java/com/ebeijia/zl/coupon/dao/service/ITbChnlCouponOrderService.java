package com.ebeijia.zl.coupon.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.coupon.dao.domain.TbChnlCouponOrder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分销商回收代金券 Service 接口类
 *
 * @User xiaomei
 * @Date 2019-02-18
 */
public interface ITbChnlCouponOrderService extends IService<TbChnlCouponOrder> {

    /**
     * 根据chnlId查询订单
     * @param chnlId
     * @return
     */
    List<TbChnlCouponOrder> getChnlCouponOrderByChnlId(String chnlId);

    /**
     * 查询分销商回收代金券订单列表
     * @param couponOrder
     * @return
     */
    List<TbChnlCouponOrder> getChnlCouponOrderList(TbChnlCouponOrder couponOrder);

    /**
     * 查询分销商回收代金券订单列表（分页）
     * @param startNum
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    PageInfo<TbChnlCouponOrder> getTbChnlCouponOrderPage(int startNum, int pageSize, TbChnlCouponOrder entity) throws Exception;

}
