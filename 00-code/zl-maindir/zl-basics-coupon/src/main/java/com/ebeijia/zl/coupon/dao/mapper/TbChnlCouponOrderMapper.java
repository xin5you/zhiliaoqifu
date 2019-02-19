package com.ebeijia.zl.coupon.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.coupon.dao.domain.TbChnlCouponOrder;
import com.ebeijia.zl.coupon.dao.domain.TbCouponHolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * 分销商回收代金券 Mapper 接口
 *
 * @User xiaomei
 * @Date 2019-02-18
 */
@Mapper
public interface TbChnlCouponOrderMapper extends BaseMapper<TbChnlCouponOrder> {

}
