package com.cn.thinkx.ecom.basics.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.thinkx.ecom.basics.goods.domain.CatGoodsRole;
import com.cn.thinkx.ecom.basics.goods.mapper.CatGoodsRoleMapper;
import com.cn.thinkx.ecom.basics.goods.service.CatGoodsRoleService;
import com.cn.thinkx.ecom.common.service.impl.BaseServiceImpl;

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
