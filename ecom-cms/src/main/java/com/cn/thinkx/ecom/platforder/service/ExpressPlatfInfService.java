package com.cn.thinkx.ecom.platforder.service;

import java.util.List;

import com.cn.thinkx.ecom.basics.order.domain.ExpressPlatf;
import com.github.pagehelper.PageInfo;

public interface ExpressPlatfInfService {

	/**
	 * 查询订单物流
	 * 
	 * @param startNum
	 * @param pageSize
	 * @param entity
	 * @return
	 */
	PageInfo<ExpressPlatf> getExpressPlatfListPage(int startNum, int pageSize, ExpressPlatf entity);
	
	/**
	 * 查看包裹货品信息
	 * 
	 * @param packId
	 * @return
	 */
	List<ExpressPlatf> getExpressPlatfProductByPackId(String packId);
}
