package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;
import java.util.Map;

import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;
import com.github.pagehelper.PageInfo;

/**
 * 分销商 可购买的产品表
 * @author zhuqiuyou
 *
 */
public interface RetailChnlProductInfFacade {

	RetailChnlProductInf getRetailChnlProductInfById(String productId) throws Exception;

	boolean saveRetailChnlProductInf(RetailChnlProductInf  retailChnlProductInf) throws Exception;
	/**
	 * 保存对象返回ID
	 * @param RetailChnlProductInf
	 * @return
	 * @throws Exception
	 */
	String saveTelChannelProductForId(RetailChnlProductInf  retailChnlProductInf) throws Exception;

	boolean updateRetailChnlProductInf(RetailChnlProductInf  retailChnlProductInf) throws Exception;

	boolean deleteRetailChnlProductInfById(String productId) throws Exception;
	
	/**
	 * 获取分销商产品的折扣
	 * @return maps --> operId:运营商，productType: 类型， areaName:地区名称，productAmt:产品面额（3位小数）
	 */
	RetailChnlProductInf getProductRateByMaps(Map maps);
	
	List<RetailChnlProductInf> getRetailChnlProductInfList(RetailChnlProductInf  retailChnlProductInf) throws Exception;
	
	PageInfo<RetailChnlProductInf> getRetailChnlProductInfPage(int startNum, int pageSize, 	RetailChnlProductInf  retailChnlProductInf) throws Exception;
	
	 /**
	  * 查询分銷商手机充值产品（带分销商的折扣率）
	  * 
	  * @param RetailChnlProductInf
	  * @return
	  */
	RetailChnlProductInf getChannelProductByItemId(String id) throws Exception;
	 
	 /**
	  * 通过分销商id获取手机充值产品
	  * 
	  * @param channelId
	  * @return
	  */
	 List<RetailChnlProductInf> getChannelProductListByChannelId(String channelId) throws Exception;

}
