package com.ebeijia.zl.coupon.dao.mapper;

import com.ebeijia.zl.coupon.dao.domain.TbCouponProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 卡密产品为卡密的大分类 Mapper 接口
 *
 * @User J
 * @Date 2019-01-05
 */
@Mapper
public interface TbCouponProductMapper extends BaseMapper<TbCouponProduct> {

    /**
     * 根据条件查询商品规格信息
     *
     * @param couponProduct
     * @return
     */
    List<TbCouponProduct> getCouponList(TbCouponProduct couponProduct);
}
