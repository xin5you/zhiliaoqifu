package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface FloorService extends BaseService<Floor> {

	/**
	 * 获取首页的楼层信息
	 * 
	 * @return
	 */
	List<Floor> getFloorGoodsList(Floor entity);
}
