package com.cn.thinkx.ecom.basics.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface SellbackGoodslistMapper extends BaseDao<SellbackGoodslist> {

	List<SellbackGoodslist> getSellbackGoodslistList(SellbackGoodslist sellbackGoodslist);
	
	/**
	 * 通过s_order_id、returns_id、o_item_id查询退货商品
	 * 
	 * @param sellbackGoodslist
	 * @return
	 */
	List<SellbackGoodslist> getSellbackGoodslistBy(SellbackGoodslist sellbackGoodslist);
	
	void updateSellbackGoodslistByReturnsId(SellbackGoodslist sellbackGoodslist);

}
