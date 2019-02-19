package com.ebeijia.zl.coupon.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.CouponTransStatEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.coupon.dao.domain.TbChnlCouponOrder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.mapper.TbChnlCouponOrderMapper;
import com.ebeijia.zl.coupon.dao.mapper.TbCouponHolderMapper;
import com.ebeijia.zl.coupon.dao.service.ITbChnlCouponOrderService;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * 分销商回收代金券 Service 实现类
 *
 * @User xiaomei
 * @Date 2019-02-18
 */
@Service
public class TbChnlCouponOrderService extends ServiceImpl<TbChnlCouponOrderMapper, TbChnlCouponOrder> implements ITbChnlCouponOrderService {

    private static Logger logger = LoggerFactory.getLogger(TbChnlCouponOrderService.class);

    @Override
    public List<TbChnlCouponOrder> getChnlCouponOrderByChnlId(String chnlId) {
        QueryWrapper<TbChnlCouponOrder> queryWrapper = new QueryWrapper<TbChnlCouponOrder>();
        queryWrapper.eq("chnl_id", chnlId);
        queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<TbChnlCouponOrder> getChnlCouponOrderList(TbChnlCouponOrder couponOrder) {
        QueryWrapper<TbChnlCouponOrder> queryWrapper = new QueryWrapper<TbChnlCouponOrder>();
        if (!StringUtil.isNullOrEmpty(couponOrder.getId())) {
            queryWrapper.eq("id", couponOrder.getId());
        }
        if (!StringUtil.isNullOrEmpty(couponOrder.getChnlId())) {
            queryWrapper.eq("chnl_id", couponOrder.getChnlId());
        }
        if (!StringUtil.isNullOrEmpty(couponOrder.getTransStat())) {
            queryWrapper.eq("trans_stat", couponOrder.getTransStat());
        }
        if (!StringUtil.isNullOrEmpty(couponOrder.getCouponBid())) {
            queryWrapper.eq("coupon_bid", couponOrder.getCouponBid());
        }
        queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public PageInfo<TbChnlCouponOrder> getTbChnlCouponOrderPage(int startNum, int pageSize, TbChnlCouponOrder entity) throws Exception {
        List<TbChnlCouponOrder> couponOrderList = this.getChnlCouponOrderList(entity);
        PageHelper.startPage(startNum, pageSize);
        for (TbChnlCouponOrder o : couponOrderList) {
            o.setCouponBid(SpecAccountTypeEnum.findByBId(o.getCouponBid()).getName());
            o.setTotalAmt(o.getTotalAmt().divide(new BigDecimal(100)));
            if ("00".equals(o.getTransStat())) {
                o.setTransStat("交易完成");
            } else if ("99".equals(o.getTransStat())) {
                o.setTransStat("交易失败");
            }
        }
        PageInfo<TbChnlCouponOrder> page = new PageInfo<TbChnlCouponOrder>(couponOrderList);
        return page;
    }
}
