package com.cn.thinkx.ecom.basics.goods.service;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface FloorService extends BaseService<Floor> {

	/**
	 * 获取首页的楼层信息
	 * 
	 * @return
	 */
	List<Floor> getFloorGoodsList(Floor entity);
}
