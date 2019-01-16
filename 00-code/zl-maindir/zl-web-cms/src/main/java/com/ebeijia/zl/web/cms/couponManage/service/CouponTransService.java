package com.ebeijia.zl.web.cms.couponManage.service;

import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
import com.github.pagehelper.PageInfo;

public interface CouponTransService {


    /**
     * 卡券流水列表（分页）
     * @param startNum
     * @param pageSize
     * @param couponTransLog
     * @return
     */
    PageInfo<TbCouponTransLog> getCouponListPage(int startNum, int pageSize, TbCouponTransLog couponTransLog);
}
