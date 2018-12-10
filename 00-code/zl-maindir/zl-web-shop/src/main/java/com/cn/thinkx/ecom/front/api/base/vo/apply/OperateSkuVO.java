package com.cn.thinkx.ecom.front.api.base.vo.apply;

import java.util.List;

/**
 * OperateSku 此sku的售后信息
 * @author zhuqiuyou
 *
 */
public class OperateSkuVO implements java.io.Serializable{

	private static final long serialVersionUID = -837346805080810482L;

	private String skuId;
	
	private Integer status;
	
	private String reason;
	
	private Integer exceptionType;
	
	private List<OrderRefundApplySkuQualityVO> qualityList;  //商品质检信息

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(Integer exceptionType) {
		this.exceptionType = exceptionType;
	}

	public List<OrderRefundApplySkuQualityVO> getQualityList() {
		return qualityList;
	}

	public void setQualityList(List<OrderRefundApplySkuQualityVO> qualityList) {
		this.qualityList = qualityList;
	}
}
