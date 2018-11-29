package com.cn.thinkx.ecom.basics.goods.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.goods.domain.SpecValues;
import com.cn.thinkx.ecom.common.service.BaseService;


/**
 * 商品规格值service
 * 
 * @author zhuqiuyou
 */
public interface SpecValuesService extends BaseService<SpecValues> {
	
	/**
	 * 查找规格值表
	 * @param record
	 * @return
	 * @throws Exception
	 */
	SpecValues getSpecValuesByRecord(SpecValues record) throws Exception;
	
	/**保存规格值表
	 * @param record
	 * @return
	 * @throws Exception
	 */
	SpecValues saveSpecValues(SpecValues record) throws Exception;
	
	/**
	 * 查找所有的规格值信息表
	 * @param record
	 * @return
	 * @throws Exception
	 */
	List<SpecValues> getList(SpecValues record) throws Exception;
	
	
	/**
	 * 查找商品的货品的规格值
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	List<SpecValues> getGoodsSpecByGoodsId(String goodsId) throws Exception;

}
