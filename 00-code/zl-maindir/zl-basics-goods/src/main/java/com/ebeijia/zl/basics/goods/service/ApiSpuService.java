package com.ebeijia.zl.basics.goods.service;

import com.ebeijia.zl.basics.goods.domain.ApiSpu;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

/**
 *  渠道spu保存
 * @author zhuqiuyou
 *
 */
public interface ApiSpuService extends BaseService<ApiSpu> {
	
	/**
	 * 保存渠道的Spu字符串信息
	 * @param spuList 集合对象
	 * @throws Exception
	 */
	void insertApiSpuList(List<ApiSpu> spuList) throws Exception;

	/**
	 *  获取渠道的spu信息
	 * @param ecomCode
	 * @param spuCode
	 * @return ApiSpu
	 * @throws Exception
	 */
	ApiSpu  getApiSpuByEcomCodeAndSpu(String ecomCode,String spuCode)  throws Exception;
	
	/**
	 * 保存渠道spu信息
	 * @return 0:未成功 1：成功
	 * @throws Exception
	 */
	int insertApiSpu(ApiSpu apiSpu)throws Exception;
	
	List<ApiSpu> getApiByIdItems(List<String> idItems);
}
