package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespDomain;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.github.pagehelper.PageInfo;


public interface RetailChnlOrderInfFacade {

	RetailChnlOrderInf getRetailChnlOrderInfById(String channelOrderId) throws Exception;

	boolean saveRetailChnlOrderInf(RetailChnlOrderInf  RetailChnlOrderInf) throws Exception;

	boolean updateRetailChnlOrderInf(RetailChnlOrderInf  RetailChnlOrderInf) throws Exception;

	boolean deleteRetailChnlOrderInfById(String channelOrderId) throws Exception;
	
	/**
	 * 分销商话费充值扣款
	 * @param retailChnlOrderInf
	 * @param operId
	 * @param areaName
	 * @return
	 * @throws Exception
	 */
	BaseResult<TeleRespVO> proChannelOrder(RetailChnlOrderInf retailChnlOrderInf, String operId, String areaName) throws Exception;
	
	List<RetailChnlOrderInf> getRetailChnlOrderInfList(RetailChnlOrderInf  retailChnlOrderInf) throws Exception;
	
	void doRechargeMobileMsg(String channelOrderId);
	
	/**
	 *  分销商 根据外部订单查询
	 * @param outerId
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	RetailChnlOrderInf getRetailChnlOrderInfByOuterId(String outerId,String channelId) throws Exception;
	
	/**
	 * 分销商订单分页列表
	 * @param startNum
	 * @param pageSize
	 * @param RetailChnlOrderInf
	 * @return  Exception
	 */
     PageInfo<RetailChnlOrderInf> getRetailChnlOrderInfPage(int startNum, int pageSize, RetailChnlOrderInf RetailChnlOrderInf) throws Exception;
     
     PageInfo<RetailChnlOrderInf> getRetailChnlOrderInf(int startNum, int pageSize, RetailChnlOrderInf RetailChnlOrderInf) throws Exception;
     
//     List<RetailChnlOrderInfUpload> getRetailChnlOrderInfListToUpload(RetailChnlOrderInf order);
     
     RetailChnlOrderInf getRetailChnlOrderInfCount(RetailChnlOrderInf order);
}
