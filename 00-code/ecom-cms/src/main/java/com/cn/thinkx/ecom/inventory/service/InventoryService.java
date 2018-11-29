package com.cn.thinkx.ecom.inventory.service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.github.pagehelper.PageInfo;

public interface InventoryService {

	PageInfo<GoodsProduct> getInventoryPage(int startNum, int pageSize, GoodsProduct pro);
}
