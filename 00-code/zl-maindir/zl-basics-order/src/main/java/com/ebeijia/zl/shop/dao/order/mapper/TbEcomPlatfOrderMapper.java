package com.ebeijia.zl.shop.dao.order.mapper;

	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebeijia.zl.shop.dao.order.domain.OrderInfo;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * 订单总表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomPlatfOrderMapper extends BaseMapper<TbEcomPlatfOrder> {

	OrderInfo getOrderInfo(OrderInfo orderInfo);

	List<TbEcomPlatfOrder> getPlatfOrderList(TbEcomPlatfOrder platfOrder);
}
