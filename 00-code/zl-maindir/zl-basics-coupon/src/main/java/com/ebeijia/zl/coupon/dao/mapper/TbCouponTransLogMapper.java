package com.ebeijia.zl.coupon.dao.mapper;

	import com.ebeijia.zl.coupon.dao.domain.TbCouponTransLog;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

    import java.util.List;

/**
 *
 * 包含卡密消费、转卖交易 Mapper 接口
 *
 * @User J
 * @Date 2019-01-05
 */
@Mapper
public interface TbCouponTransLogMapper extends BaseMapper<TbCouponTransLog> {

    List<TbCouponTransLog> getTransLog(TbCouponTransLog couponTransLog);
}
