package com.ebeijia.zl.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.ebeijia.zl.coupon.service.ICouponShareService;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponShareService implements ICouponShareService {

    @Autowired
    private ITbCouponHolderService holderDao;

    @Override
    public List<TbCouponHolder> couponShare(String memberId, String couponCode, Long price, Integer amount) {
        TbCouponHolder query = new TbCouponHolder();
        query.setPrice(price);
        query.setCouponCode(couponCode);
        query.setMemberId(memberId);
        query.setDataStat("0");
        query.setTransStat("0");
        List<TbCouponHolder> holders = holderDao.list(new QueryWrapper<>(query));
        if (holders == null || amount.compareTo(holders.size()) > 0) {
            throw new BizException(ResultState.BALANCE_NOT_ENOUGH, "您的卡券数量不足");
        }

        //循环遍历，更新数据
        List<TbCouponHolder> targetHolders = holders.subList(0, amount);
        for (TbCouponHolder h : targetHolders) {
            TbCouponHolder q = new TbCouponHolder();
            q.setMemberId(h.getMemberId());
            q.setCouponId(h.getCouponId());
            q.setLockVersion(h.getLockVersion());
            h.setLockVersion(h.getLockVersion() + 1);
            h.setTransStat("1");
            h.setUpdateTime(System.currentTimeMillis());
            h.setUpdateUser("ShopSystem");
            holderDao.update(h, new QueryWrapper<>(q));
        }
        return targetHolders;
    }

}
