package com.ebeijia.zl.service.telrecharge.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderInf;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.qianmi.open.api.domain.elife.OrderDetailInfo;


/**
 *
 * 供应商订单明细表 Service 接口类
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
public interface ProviderOrderInfService extends IService<ProviderOrderInf> {

	List<ProviderOrderInf> getProviderOrderInfList(ProviderOrderInf  providerOrderInf);
	
	/**
	 * 查找updateTime 10分钟以内，1分钟以上的订单
	 * @param providerOrderInf
	 * @return
	 */
	List<ProviderOrderInf> getListByTimer(ProviderOrderInf providerOrderInf);
	
	ProviderOrderInf getOrderInfByChannelOrderId(String OrderId);

	/**
	 * 话费充值状态
	 * @param orderDetailInfo
	 */
	void updateOrderRechargeState(ProviderOrderInf telProviderOrderInf,OrderDetailInfo orderDetailInfo,String respCode);


	/**
	 *  调用供应商商接口，发起消费扣款
	 * @param providerInf
	 * @param telProviderOrderInf
	 * @return
	 */
	boolean doMchntCustomerToProvider(ProviderInf providerInf, ProviderOrderInf telProviderOrderInf) throws Exception;
}
