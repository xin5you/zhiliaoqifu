package com.ebeijia.zl.coupon.dao.service.impl;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebeijia.zl.common.utils.enums.CouponTransStatEnum;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import com.ebeijia.zl.coupon.dao.mapper.TbCouponHolderMapper;
import com.ebeijia.zl.coupon.dao.service.ITbCouponHolderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TbCouponHolderService extends ServiceImpl<TbCouponHolderMapper, TbCouponHolder> implements ITbCouponHolderService {

    private static Logger logger = LoggerFactory.getLogger(TbCouponHolderService.class);

    @Override
    public List<TbCouponHolder> listCouponHolder(TbCouponHolder holder) {
        return baseMapper.listCouponHolder(holder);
    }

    @Override
    public TbCouponHolder getCouponHolder(TbCouponHolder holder) {
        return baseMapper.getCouponHolder(holder);
    }
//
//    @Override
//    public List<TbCouponHolder> couponShare(String memberId, String couponCode, Long price, Integer amount) {
//        TbCouponHolder query = new TbCouponHolder();
//        query.setPrice(price);
//        query.setCouponCode(couponCode);
//        query.setMemberId(memberId);
//        query.setDataStat("0");
//        query.setTransStat("0");
//        List<TbCouponHolder> holders = this.list(new QueryWrapper<>(query));
//        if (holders==null || amount.compareTo(holders.size()) > 0) {
//           return null;
//        }
//
//        //循环遍历，更新数据
//        List<TbCouponHolder> targetHolders = holders.subList(0, amount);
//        for (TbCouponHolder h : targetHolders){
//            TbCouponHolder q = new TbCouponHolder();
//            q.setMemberId(h.getMemberId());
//            q.setCouponId(h.getCouponId());
//            q.setLockVersion(h.getLockVersion());
//            h.setLockVersion(h.getLockVersion() + 1);
//            h.setTransStat("1");
//            h.setUpdateTime(System.currentTimeMillis());
//            h.setUpdateUser("ShopSystem");
//            this.update(h,new QueryWrapper<>(q));
//        }
//        return targetHolders;
//    }

    @Override
    public List<TbCouponHolder> couponShareRollback(List<TbCouponHolder> list) {
        for (TbCouponHolder h : list){
            TbCouponHolder query = new TbCouponHolder();
            query.setMemberId(h.getMemberId());
            query.setCouponId(h.getCouponId());
            query.setLockVersion(h.getLockVersion());
            h.setLockVersion(h.getLockVersion() + 1);
            h.setTransStat("0");
            h.setUpdateTime(System.currentTimeMillis());
            h.setUpdateUser("ShopSystem");
            try {
                this.update(h, new QueryWrapper<>(query));
            }catch (Exception e){
                logger.error("用户持有卡券状态回滚出错",e);
            }
        }
        return list;
    }

    @Override
    public List<TbCouponHolder> listCouponHolderByCouponHolder(TbCouponHolder couponHolder) {
        QueryWrapper<TbCouponHolder> queryWrapper = new QueryWrapper<TbCouponHolder>();
        if (!StringUtil.isNullOrEmpty(couponHolder.getCouponName())) {
            queryWrapper.like("coupon_name", couponHolder.getCouponName());
        }
        if (!StringUtil.isNullOrEmpty(couponHolder.getBId())) {
            queryWrapper.like("b_id", couponHolder.getBId());
        }
        if (!StringUtil.isNullOrEmpty(couponHolder.getTransStat())) {
            queryWrapper.eq("trans_stat", couponHolder.getTransStat());
        }
        queryWrapper.eq("data_stat", DataStatEnum.TRUE_STATUS.getCode());
        queryWrapper.orderByAsc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    /*@Override
    public PageInfo<TbCouponHolder> getTbCouponHolderPage(int startNum, int pageSize, TbCouponHolder entity) throws Exception {
        PageHelper.startPage(startNum, pageSize);
        List<TbCouponHolder> list = listCouponHolderByCouponHolder(entity);
        for (TbCouponHolder h : list) {
            h.setTransStat(CouponTransStatEnum.findByBId(h.getTransStat()).getName());
            h.setBId(SpecAccountTypeEnum.findByBId(h.getBId()).getName());
        }
        PageInfo<TbCouponHolder> page = new PageInfo<TbCouponHolder>(list);
        return page;
    }*/

}
