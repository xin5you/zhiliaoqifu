package com.ebeijia.zl.facade.telrecharge.service;

import java.util.List;

import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.github.pagehelper.PageInfo;

public interface RetailChnlInfFacade {

	RetailChnlInf getRetailChnlInfById(String channelId) throws Exception;

	boolean saveRetailChnlInf(RetailChnlInf retailChnlInf) throws Exception;

	boolean updateRetailChnlInf(RetailChnlInf retailChnlInf) throws Exception;

	boolean deleteRetailChnlInfById(String channelId) throws Exception;
	
	List<RetailChnlInf> getRetailChnlInfList(RetailChnlInf retailChnlInf) throws Exception;
//	
//	/**
//	 * 扣减的渠道金额
//	 * @param payAmt 订单金额，需扣减的金额
//	 * @return
//	 */
//	int subChannelReserveAmt(String channelId,BigDecimal payAmt) throws Exception;
	
	PageInfo<RetailChnlInf> getRetailChnlInfPage(int startNum, int pageSize, RetailChnlInf retailChnlInf) throws Exception;
	
//	RetailChnlInf getRetailChnlInfByMchntCode(String mchntCode) throws Exception;

	RetailChnlInf getRetailChnlInfByLawCode(String lawCode) throws Exception;

}
