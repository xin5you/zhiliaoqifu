package com.cn.thinkx.wecard.facade.telrecharge.service;

import java.util.List;

import com.cn.thinkx.wecard.facade.telrecharge.domain.RetailChnlItemList;
import com.github.pagehelper.PageInfo;

/**
 * 话费充值，产品地区关联中间service
 * 
 * @author zhuqiuyou
 *
 */
public interface RetailChnlItemListFacade {

	RetailChnlItemList getRetailChnlItemListById(String id) throws Exception;

	boolean saveRetailChnlItemList(RetailChnlItemList retailChnlItemList) throws Exception;

	boolean updateRetailChnlItemList(RetailChnlItemList retailChnlItemList) throws Exception;

	boolean deleteRetailChnlItemListById(String id) throws Exception;

	int deleteByProductId(String id) throws Exception;

	List<RetailChnlItemList> getRetailChnlItemList(RetailChnlItemList retailChnlItemList) throws Exception;

	PageInfo<RetailChnlItemList> getRetailChnlItemListPage(int startNum, int pageSize,
			RetailChnlItemList retailChnlItemList) throws Exception;
}
