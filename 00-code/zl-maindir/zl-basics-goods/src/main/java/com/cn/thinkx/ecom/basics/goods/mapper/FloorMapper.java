package com.cn.thinkx.ecom.basics.goods.mapper;

import com.cn.thinkx.ecom.basics.goods.domain.Floor;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FloorMapper extends BaseDao<Floor> {

	/**
	 * 获取首页的楼层对应的商品信息(六条)
	 * 
	 * @return
	 */
	List<Floor> getFloorGoodsList(Floor entity);

	/**
	 * 获取首页点击更多的楼层对应的商品信息（全部）
	 * 
	 * @param entity
	 * @return
	 */
	List<Floor> getFloorGoodsListAll(Floor entity);

}
