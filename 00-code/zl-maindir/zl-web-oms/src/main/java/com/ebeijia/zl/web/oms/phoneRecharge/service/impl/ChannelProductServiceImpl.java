package com.ebeijia.zl.web.oms.phoneRecharge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlProductInfFacade;
import com.ebeijia.zl.web.oms.phoneRecharge.service.ChannelProductService;

@Service("channelProductService")
public class ChannelProductServiceImpl implements ChannelProductService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Reference(check = false, version = "1.0.0")
	private RetailChnlProductInfFacade telChannelProductInfFacade;

	@Reference(check = false, version = "1.0.0")
	private RetailChnlItemListFacade telChannelItemListFacade;

	@Override
	public int deleteTelChannelProductInf(String productId) {
		try {
			telChannelProductInfFacade.deleteRetailChnlProductInfById(productId);
			telChannelItemListFacade.deleteByProductId(productId);
		} catch (Exception e) {
			logger.error("## 删除分销商产品信息异常", e);
		}
		return 0;
	}

	
}
