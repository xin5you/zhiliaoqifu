package com.ebeijia.zl.shop.dao.order.mapper;

	import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
	import com.baomidou.mybatisplus.core.mapper.BaseMapper;
	import org.apache.ibatis.annotations.Mapper;

	import java.util.List;

/**
 *
 * 渠道子订单表 Mapper 接口
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Mapper
public interface TbEcomPlatfShopOrderMapper extends BaseMapper<TbEcomPlatfShopOrder> {

	List<TbEcomPlatfShopOrder> getPlatfShopOrderListByPlatfOrder(TbEcomPlatfShopOrder platfShopOrder);

	List<TbEcomPlatfShopOrder> getPlatfShopOrderList(TbEcomPlatfShopOrder platfShopOrder);

}
