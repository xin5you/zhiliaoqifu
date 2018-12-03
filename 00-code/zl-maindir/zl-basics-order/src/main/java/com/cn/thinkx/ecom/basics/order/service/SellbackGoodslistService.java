package com.cn.thinkx.ecom.basics.order.service;

import com.cn.thinkx.ecom.basics.order.domain.SellbackGoodslist;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface SellbackGoodslistService extends BaseService<SellbackGoodslist> {

	List<SellbackGoodslist> getSellbackGoodslistList(SellbackGoodslist sellbackGoodslist);
	
	List<SellbackGoodslist> getSellbackGoodslistBy(SellbackGoodslist sellbackGoodslist);
	
	void updateSellbackGoodslistByReturnsId(SellbackGoodslist sellbackGoodslist);
	
}
