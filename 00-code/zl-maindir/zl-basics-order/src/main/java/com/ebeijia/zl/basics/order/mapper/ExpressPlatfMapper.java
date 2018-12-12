package com.ebeijia.zl.basics.order.mapper;

import com.ebeijia.zl.basics.order.domain.ExpressPlatf;
import com.ebeijia.zl.common.core.mapper.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExpressPlatfMapper extends BaseDao<ExpressPlatf> {

	List<ExpressPlatf> getExpressPlatfList(ExpressPlatf ep);
	
	/**
	 * 根据渠道号和包裹Id查询订单包裹信息
	 * @param ecomCode 商户标识
	 * @param packageNo 包裹号
	 * @return
	 */
	ExpressPlatf selectByEcomAndPackageNo (@Param("ecomCode")String ecomCode,@Param("packageNo") String packageNo);
	
	/**
	 * 查询物流货品信息
	 * 
	 * @param packId
	 * @return
	 */
	List<ExpressPlatf> getExpressPlatfProductByPackId(String packId);
	
	/**
	 * 通过二级订单号查询物流信息
	 * 
	 * @param sOrderId
	 * @return
	 */
	List<ExpressPlatf> getOrderExpressPlatfBySOrderId(String sOrderId);
	
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
