package com.cn.thinkx.ecom.basics.order.mapper;

import com.cn.thinkx.ecom.basics.order.domain.OrderExpressPlatf;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderExpressPlatfMapper extends BaseDao<OrderExpressPlatf> {

	OrderExpressPlatf getOrderExpressPlatfByPackId(@Param("packId")String packId,
																				 @Param("oItemId")String oItemId,
																				 @Param("skuCode")String skuCode);
}
