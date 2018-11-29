package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.FloorGoods;
import com.cn.thinkx.ecom.basics.goods.mapper.FloorGoodsMapper;
import com.cn.thinkx.ecom.basics.goods.service.FloorGoodsService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;
@Service
public class FloorGoodsServiceImpl extends BaseServiceImpl<FloorGoods> implements FloorGoodsService{

	@Autowired
	private FloorGoodsMapper floorGoodsMapper;
	
	@Override
	public List<FloorGoods> getFloorGoods(FloorGoods entity) {
		return floorGoodsMapper.getFloorGoods(entity);
	}

	@Override
	public List<FloorGoods> getGoods(FloorGoods entity) {
		return floorGoodsMapper.getGoods(entity);
	}

	@Override
	public int deleteByFloorIdAndGoodsId(FloorGoods entity) {
		return floorGoodsMapper.deleteByFloorIdAndGoodsId(entity);
	}

}
