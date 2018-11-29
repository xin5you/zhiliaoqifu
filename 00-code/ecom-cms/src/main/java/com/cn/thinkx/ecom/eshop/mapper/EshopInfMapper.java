package com.cn.thinkx.ecom.eshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.thinkx.ecom.common.mapper.BaseDao;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;

@Mapper
public interface EshopInfMapper extends BaseDao<EshopInf> {

	/**
	 * 查询所有商城信息
	 * 
	 * @return
	 */
	List<EshopInf> getList();

	/**
	 * 查询tb_mchnt_eshop_inf中所有商城信息
	 * 
	 * @return
	 */
	List<EshopInf> selectByComboBox();
	
	/**
	 * 根据商户名称查询tb_mchnt_eshop_inf中商城信息
	 * 
	 * @param eshopInf
	 * @return
	 */
	EshopInf selectByEshopName(EshopInf eshopInf);

	/**
	 * 根据条件查询商城信息
	 * 
	 * @param eshopInf
	 * @return
	 */
	EshopInf selectByEshopInf(EshopInf eshopInf);

}
