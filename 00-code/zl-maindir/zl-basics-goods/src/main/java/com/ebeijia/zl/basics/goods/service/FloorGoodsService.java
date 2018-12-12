package com.ebeijia.zl.basics.goods.service;

import java.util.List;

import com.ebeijia.zl.basics.goods.domain.FloorGoods;
import com.ebeijia.zl.common.core.service.BaseService;

public interface FloorGoodsService extends BaseService<FloorGoods> {


	/**
	 * 查询楼层中选中商品
	 * 
	 * @return
	 */
	List<FloorGoods> getFloorGoods(FloorGoods entity);
	
	/**
	 * 查询楼层中没有选中商品
	 * 
	 * @return
	 */
	List<FloorGoods> getGoods(FloorGoods entity);
	
	/**
	 * 删除楼层对应的商品
	 * 
	 * @param entity
	 * @return
	 */
	int deleteByFloorIdAndGoodsId(FloorGoods entity);
}
