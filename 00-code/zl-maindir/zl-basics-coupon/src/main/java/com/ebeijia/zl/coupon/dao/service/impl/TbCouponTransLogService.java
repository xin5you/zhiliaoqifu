package com.ebeijia.zl.coupon.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.ebeijia.zl.coupon.dao.mapper.TbCouponTransLogMapper;
import com.ebeijia.zl.coupon.dao.service.ITbCouponTransLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * 包含卡密消费、转卖交易 Service 实现类
 *
 * @User J
 * @Date 2019-01-05
 */
@Service
public class TbCouponTransLogService extends ServiceImpl<TbCouponTransLogMapper, TbCouponTransLog> implements ITbCouponTransLogService {


    @Autowired
    private TbCouponTransLogMapper couponTransLogMapper;


    @Override
    public List<TbCouponTransLog> getCouponTransLogs(TbCouponTransLog couponTransLog) {
        return couponTransLogMapper.getTransLog(couponTransLog);
    }
}
