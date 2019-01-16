package com.ebeijia.zl.web.cms.couponManage.service;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface CouponManageService {


    /**
     * 卡券信息列表（分页）
     * @param startNum
     * @param pageSize
     * @param couponProduct
     * @return
     */
    PageInfo<TbCouponProduct> getCouponListPage(int startNum, int pageSize, TbCouponProduct couponProduct);


    /**
     * 卡券值新增
     * @param entity
     * @param iconImageFile
     * @return
     */
    BaseResult<Object> addCouponValues(TbCouponProduct entity, MultipartFile iconImageFile);

    /**
     * 卡券值编辑
     * @param couponProduct
     * @param iconImageFile
     * @return
     */
    BaseResult<Object> editCouponValues(TbCouponProduct couponProduct, MultipartFile iconImageFile);


    /**
     * 卡券删除
     * @param couponProduct
     * @return
     */
    BaseResult<Object> deleteGoodsSpec(TbCouponProduct couponProduct);
}
