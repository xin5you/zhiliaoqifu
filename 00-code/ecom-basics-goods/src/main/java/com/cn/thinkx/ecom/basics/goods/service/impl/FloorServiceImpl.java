package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.mapper.FloorMapper;
import com.cn.thinkx.ecom.basics.goods.service.FloorService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

@Service("floorService")
public class FloorServiceImpl extends BaseServiceImpl<Floor> implements FloorService {

	@Autowired
	private FloorMapper floorMapper;

	@Override
	public List<Floor> getFloorGoodsList(Floor entity) {
		return floorMapper.getFloorGoodsList(entity);
	}

}
