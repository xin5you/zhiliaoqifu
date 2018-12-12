package com.ebeijia.zl.basics.goods.service;

import java.util.List;

import com.ebeijia.zl.basics.goods.domain.Floor;
import com.ebeijia.zl.common.core.service.BaseService;

public interface FloorService extends BaseService<Floor> {

	/**
	 * 获取首页的楼层信息
	 * 
	 * @return
	 */
	List<Floor> getFloorGoodsList(Floor entity);
}
