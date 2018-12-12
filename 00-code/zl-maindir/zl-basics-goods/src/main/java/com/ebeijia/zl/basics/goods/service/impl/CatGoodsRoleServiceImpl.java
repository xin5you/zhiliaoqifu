package com.ebeijia.zl.basics.goods.service.impl;

import com.ebeijia.zl.basics.goods.domain.CatGoodsRole;
import com.ebeijia.zl.basics.goods.mapper.CatGoodsRoleMapper;
import com.ebeijia.zl.basics.goods.service.CatGoodsRoleService;
import com.ebeijia.zl.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("catGoodsRoleService")
public class CatGoodsRoleServiceImpl extends BaseServiceImpl<CatGoodsRole> implements CatGoodsRoleService {

	@Autowired
	private	 CatGoodsRoleMapper catGoodsRoleMapper;
	

	/**
	 * 保存商品类目关联关系
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void saveCatGoodsRole(List<String> catIds,String goodsId)throws Exception{
		
		CatGoodsRole catGoodsRole=null;
		if(catIds !=null && catIds.size()>0 && goodsId !=null){
			for(String catId:catIds){
				catGoodsRole=catGoodsRoleMapper.getCatGoodsByCarIdAndGoodsId(catId, goodsId);
				if(catGoodsRole ==null){
					catGoodsRole=new CatGoodsRole();
					catGoodsRole.setCatId(catId);
					catGoodsRole.setGoodsId(goodsId);
					catGoodsRoleMapper.insert(catGoodsRole);
				}
			}
		}
	}

}
