package com.cn.thinkx.ecom.floor.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.cn.thinkx.ecom.basics.goods.domain.FloorGoods;
import com.cn.thinkx.ecom.common.domain.BaseResult;
import com.cn.thinkx.ecom.common.service.BaseService;
import com.github.pagehelper.PageInfo;

public interface FloorInfService extends BaseService<Floor> {

	/**
	 * 查询电商楼层信息（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<Floor> getFloorListPage(int startNum, int pageSize, Floor entity);

	/**
	 * 添加楼层商品
	 * 
	 * @return
	 */
	boolean addFloorEgoods(String floorId, String ids, HttpServletRequest req);
	
	/**
	 * 查询对应的楼层商品数据
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<FloorGoods> getFloorGoodsListPage(int startNum, int pageSize, FloorGoods entity, String type);
	
	/**
	 * 删除楼层商品信息
	 * 
	 * @param req
	 * @return
	 */
	BaseResult<Object> deleteFloorGoods(HttpServletRequest req);
	
	/**
	 * 删除楼层信息
	 * 
	 * @param req
	 * @return
	 */
	BaseResult<Object> deleteFloor(HttpServletRequest req);

}
