package com.cn.thinkx.ecom.inventory.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.GoodsProduct;
import com.cn.thinkx.ecom.basics.goods.service.GoodsProductService;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsMarketType;
import com.cn.thinkx.ecom.inventory.service.InventoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	private GoodsProductService goodsProductService;

	@Override
	public PageInfo<GoodsProduct> getInventoryPage(int startNum, int pageSize, GoodsProduct pro) {
		PageHelper.startPage(startNum, pageSize);
		List<GoodsProduct> list = goodsProductService.getInventoryList(pro);
		PageInfo<GoodsProduct> page = new PageInfo<GoodsProduct>(list);
		page.getList().stream().filter(p -> {
			p.setEcomType(GoodsEcomCodeType.findByCode(p.getEcomCode()).getValue());
			p.setMarketType(GoodsMarketType.findByCode(p.getMarketEnable()).getValue());
			return true;
		}).collect(Collectors.toList());
		return page;
	}

}
