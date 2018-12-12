package com.ebeijia.zl.basics.goods.service;

import com.ebeijia.zl.basics.goods.domain.CatGoodsRole;
import com.ebeijia.zl.common.core.service.BaseService;

import java.util.List;

public interface CatGoodsRoleService extends BaseService<CatGoodsRole> {


	/**
	 * 保存商品类目关联关系
	 * @param category
	 * @return
	 * @throws Exception
	 */
	void saveCatGoodsRole(List<String> catIds,String goodsId)throws Exception;
}
