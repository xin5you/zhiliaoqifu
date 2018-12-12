package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.Floor;
import com.ebeijia.zl.basics.goods.mapper.FloorMapper;
import com.ebeijia.zl.basics.goods.service.FloorService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("floorService")
public class FloorServiceImpl extends BaseServiceImpl<Floor> implements FloorService {

	@Autowired
	private FloorMapper floorMapper;

	@Override
	public List<Floor> getFloorGoodsList(Floor entity) {
		return floorMapper.getFloorGoodsList(entity);
	}

}
