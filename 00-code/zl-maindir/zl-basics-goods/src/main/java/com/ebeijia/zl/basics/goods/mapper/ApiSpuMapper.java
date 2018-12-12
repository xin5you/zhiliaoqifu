package com.ebeijia.zl.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ebeijia.zl.basics.goods.domain.ApiSpu;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface ApiSpuMapper  extends BaseDao<ApiSpu> {
	
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
