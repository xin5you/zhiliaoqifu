package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlProductInf;


/**
 *
 * 分销商充值产品管理表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface RetailChnlProductInfService extends IService<RetailChnlProductInf> {
	
	RetailChnlProductInf getProductRateByMaps(Map maps);

	/**
	 * 查看分销商的折扣率
	 * 
	 * @param id
	 * @return
	 */
	RetailChnlProductInf getChannelProductByItemId(String id);

	/**
	 * 通过分销商id获取手机充值产品
	 * 
	 * @param channelId
	 * @return
	 */
	List<RetailChnlProductInf> getChannelProductListByChannelId(String channelId);
	
	
	List<RetailChnlProductInf> getList(RetailChnlProductInf retailChnlProductInf);

}
