package com.ebeijia.zl.web.oms.retailChnl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlProductService;

@Service("retailChnlProductService")
public class RetailChnlProductServiceImpl implements RetailChnlProductService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RetailChnlProductInfFacade telChannelProductInfFacade;

	@Autowired
	private RetailChnlItemListFacade telChannelItemListFacade;

	@Override
	public int deleteRetailChnlProductInf(String productId) {
		try {
			telChannelProductInfFacade.deleteRetailChnlProductInfById(productId);
			telChannelItemListFacade.deleteByProductId(productId);
		} catch (Exception e) {
			logger.error("## 删除分销商产品信息异常", e);
		}
		return 0;
	}

	
}
