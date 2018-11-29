package com.cn.thinkx.ecom.basics.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.order.domain.OrderExpressPlatf;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface OrderExpressPlatfMapper extends BaseDao<OrderExpressPlatf>{

	OrderExpressPlatf getOrderExpressPlatfByPackId(@Param("packId")String packId,
																				 @Param("oItemId")String oItemId,
																				 @Param("skuCode")String skuCode);
}
