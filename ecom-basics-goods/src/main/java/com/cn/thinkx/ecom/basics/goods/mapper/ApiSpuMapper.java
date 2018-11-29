package com.cn.thinkx.ecom.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cn.thinkx.ecom.basics.goods.domain.ApiSpu;
import com.cn.thinkx.ecom.common.mapper.BaseDao;

@Mapper
public interface ApiSpuMapper  extends BaseDao<ApiSpu>  {
	
	ApiSpu getApiSpuBySpuCode(String spuCode);
	/**
	 *  获取渠道的spu信息
	 * @param ecomCode
	 * @param spuCode
	 * @return ApiSpu
	 */
	ApiSpu  getApiSpuByEcomCodeAndSpu(@Param("ecomCode")String ecomCode,@Param("spuCode")String spuCode);
	
	List<ApiSpu> getApiByIdItems(List<String> idItems);
}
