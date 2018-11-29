package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.CatGoodsRole;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface CatGoodsRoleService extends BaseService<CatGoodsRole> {


	/**
	 * 保存商品类目关联关系
	 * @param category
	 * @return
	 * @throws Exception
	 */
	void saveCatGoodsRole(List<String> catIds,String goodsId)throws Exception;
}
