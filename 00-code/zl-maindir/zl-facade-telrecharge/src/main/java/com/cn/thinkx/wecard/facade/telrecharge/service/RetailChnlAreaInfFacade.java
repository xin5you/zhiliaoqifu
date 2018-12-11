package com.cn.thinkx.wecard.facade.telrecharge.service;

import java.util.List;

import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlAreaInf;

/**
 * 话费充值，地区维护service
 * @author zhuqiuyou
 *
 */
public interface RetailChnlAreaInfFacade {

	RetailChnlAreaInf getRetailChnlAreaInfById(String area_id) throws Exception;

	boolean saveRetailChnlAreaInf(RetailChnlAreaInf  retailChnlAreaInf) throws Exception;

	boolean updateRetailChnlAreaInf(RetailChnlAreaInf  retailChnlAreaInf) throws Exception;

	boolean deleteRetailChnlAreaInfById(String areaId) throws Exception;
	
	List<RetailChnlAreaInf> getRetailChnlAreaInfList(RetailChnlAreaInf  retailChnlAreaInf) throws Exception;
}
