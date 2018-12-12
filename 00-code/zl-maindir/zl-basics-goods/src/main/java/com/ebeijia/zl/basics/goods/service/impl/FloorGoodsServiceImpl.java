package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.FloorGoods;
import com.ebeijia.zl.basics.goods.mapper.FloorGoodsMapper;
import com.ebeijia.zl.basics.goods.service.FloorGoodsService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
