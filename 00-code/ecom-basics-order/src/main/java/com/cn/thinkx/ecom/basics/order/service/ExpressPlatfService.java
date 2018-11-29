package com.cn.thinkx.ecom.basics.order.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.cn.thinkx.ecom.common.service.BaseService;

public interface ExpressPlatfService extends BaseService<ExpressPlatf> {

	List<ExpressPlatf> getExpressPlatfList(ExpressPlatf ep);
	
	/**
	 * 根据渠道号和包裹Id查询订单包裹信息
	 * @param ecomCode 商户标识
	 * @param packageNo 包裹号
	 * @return
	 */
	ExpressPlatf selectByEcomAndPackageNo (String ecomCode, String packageNo);
	
	/**
	 * 保存订单包裹信息
	 * @param ep
	 * @return
	 */
	ExpressPlatf saveExpressPlatfForPackageNo(ExpressPlatf ep);
	
	/**
	 * 通过二级订单号查询物流信息
	 * 
	 * @param sOrderId
	 * @return
	 */
	List<ExpressPlatf> getOrderExpressPlatfBySOrderId(String sOrderId);
	
	/**
	 * 查询物流货品信息
	 * 
	 * @param packId
	 * @return
	 */
	List<ExpressPlatf> getExpressPlatfProductByPackId(String packId);
	
	/**
	 * 查询当前时间前15天的数据
	 * 
	 * @return
	 */
	List<ExpressPlatf> getExpressPlatfBySignTimeJob();
	
	/**
	 * 通过二级订单号和订单货品明细id查询在45天内的发货时间信息
	 * 
	 * @param ep
	 * @return
	 */
	List<ExpressPlatf> getDeliveryTimeByItemIdAndSorderId(ExpressPlatf ep);
}
