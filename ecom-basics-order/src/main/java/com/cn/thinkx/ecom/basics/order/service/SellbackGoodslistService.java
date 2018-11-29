package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface SellbackGoodslistService extends BaseService<SellbackGoodslist> {

	List<SellbackGoodslist> getSellbackGoodslistList(SellbackGoodslist sellbackGoodslist);
	
	List<SellbackGoodslist> getSellbackGoodslistBy(SellbackGoodslist sellbackGoodslist);
	
	void updateSellbackGoodslistByReturnsId(SellbackGoodslist sellbackGoodslist);
	
}
