package com.ebeijia.zl.basics.coupon.mapper;

	import com.ebeijia.zl.basics.coupon.domain.TbCouponTransLog;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

/**
 *
 * 包含卡密消费、转卖交易 Mapper 接口
 *
 * @User J
 * @Date 2018-12-13
 */
@Mapper
public interface TbCouponTransLogMapper extends BaseMapper<TbCouponTransLog> {
	
}
