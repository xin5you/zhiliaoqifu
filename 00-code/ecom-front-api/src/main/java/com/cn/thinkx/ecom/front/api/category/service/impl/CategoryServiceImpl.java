package com.cn.thinkx.ecom.front.api.category.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.basics.goods.service.GoodsService;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsEcomCodeType;
import com.cn.thinkx.ecom.common.constants.Constants.GoodsSkuSyncType;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.front.api.category.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private GoodsService goodsService;
	
	@Override
	public PageInfo<Goods> getGoodsPage(int startNum, int pageSize, Goods entity) {
		PageHelper.startPage(startNum, pageSize);
		List<Goods> list = null;
		try {
			list = goodsService.getGoodsByCategory(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<Goods> page = new PageInfo<Goods>(list);
		page.getList().stream().filter(goods ->{
			if(!StringUtil.isNullOrEmpty(goods.getGoodsPrice())){
				goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
			}
			return true;
		}).collect(Collectors.toList());
		return page;
	}

	@Override
	public List<Goods> getGoodsByGoods(Goods entity) {
		List<Goods> list = goodsService.getGoodsByCategory(entity);
		list.stream().filter(goods ->{
			if(!StringUtil.isNullOrEmpty(goods.getGoodsPrice())){
				goods.setGoodsPrice(NumberUtils.RMBCentToYuan(goods.getGoodsPrice()));
			}
			return true;
		}).collect(Collectors.toList());
		return list;
	}

}
