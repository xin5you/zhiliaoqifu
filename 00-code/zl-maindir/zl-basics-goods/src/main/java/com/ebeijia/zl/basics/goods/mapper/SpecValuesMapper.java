package com.ebeijia.zl.basics.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ebeijia.zl.basics.goods.domain.SpecValues;
import com.ebeijia.zl.common.core.mapper.BaseDao;

@Mapper
public interface SpecValuesMapper  extends BaseDao<SpecValues> {
	
	/**
	 * 查找规格值表
	 * @param record
	 * @return
	 * @throws Exception
	 */
	SpecValues getSpecValuesByRecord(SpecValues record) throws Exception;
	
	/**
	 * 查找商品的货品的规格值
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	List<SpecValues> getGoodsSpecByGoodsId(String goodsId) throws Exception;
}
