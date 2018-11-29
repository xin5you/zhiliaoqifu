package com.cn.thinkx.ecom.eshop.service;

import java.util.List;

import com.cn.thinkx.ecom.common.service.BaseService;
import com.cn.thinkx.ecom.eshop.domain.EshopInf;
import com.github.pagehelper.PageInfo;

public interface EshopInfService extends BaseService<EshopInf> {

	/**
	 * 商城列表（含分页）
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<EshopInf> getEshopInfPage(int startNum, int pageSize, EshopInf entity);

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
