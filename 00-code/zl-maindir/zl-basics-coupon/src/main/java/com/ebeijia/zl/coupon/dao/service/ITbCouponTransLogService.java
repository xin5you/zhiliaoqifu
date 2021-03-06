package com.ebeijia.zl.coupon.dao.service;

import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 包含卡密消费、转卖交易 Service 接口类
 *
 * @User J
 * @Date 2019-01-05
 */
public interface ITbCouponTransLogService extends IService<TbCouponTransLog> {

    /**
     * 根据条件查询卡券流水信息
     * @param couponTransLog
     * @return
     */
    List<TbCouponTransLog> getCouponTransLogs(TbCouponTransLog couponTransLog);
}
