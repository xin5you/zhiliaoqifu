package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.util.List;

/**
 * OrderPackage 订单包裹
 * @author zhuqiuyou
 *
 */
public class OrderPackageVO implements java.io.Serializable {

	private static final long serialVersionUID = 3780108837470007443L;

	private String packageId;
	
	private String expressCompany;
	
	private String expressNo;//运单号
	
	private String expCreateTime;//发货时间
	
	private String confirmTime;//签收时间
	
	private String packageStatus;
	
	private List<OrderSkuVO> orderSkus;
	
	private List<ExpressDetailInfoVO> expressDetailInfos; //物流详情信息

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpCreateTime() {
		return expCreateTime;
	}

	public void setExpCreateTime(String expCreateTime) {
		this.expCreateTime = expCreateTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}

	public List<OrderSkuVO> getOrderSkus() {
		return orderSkus;
	}

	public void setOrderSkus(List<OrderSkuVO> orderSkus) {
		this.orderSkus = orderSkus;
	}

	public List<ExpressDetailInfoVO> getExpressDetailInfos() {
		return expressDetailInfos;
	}

	public void setExpressDetailInfos(List<ExpressDetailInfoVO> expressDetailInfos) {
		this.expressDetailInfos = expressDetailInfos;
	}
}
