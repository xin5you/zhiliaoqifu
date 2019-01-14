package com.ebeijia.zl.web.cms.couponManage.service.impl;

import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.ebeijia.zl.coupon.dao.service.ITbCouponTransLogService;
import com.ebeijia.zl.web.cms.couponManage.service.CouponTransService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("couponTransService")
public class CouponTransServiceImpl implements CouponTransService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ITbCouponTransLogService couponProductLogservice;


    @Override
    public PageInfo<TbCouponTransLog> getCouponListPage(int startNum, int pageSize, TbCouponTransLog couponTransLog) {
        List<TbCouponTransLog> goodsSpecList = new ArrayList<>();

        PageHelper.startPage(startNum, pageSize);
        List<TbCouponTransLog> transLogs = couponProductLogservice.getCouponTransLogs(couponTransLog);
        PageInfo<TbCouponTransLog> page = new PageInfo<>(transLogs);
        return page;
    }

}
