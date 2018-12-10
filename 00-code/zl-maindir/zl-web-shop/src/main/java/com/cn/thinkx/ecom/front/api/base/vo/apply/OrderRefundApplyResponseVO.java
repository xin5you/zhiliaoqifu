package com.cn.thinkx.ecom.front.api.base.vo.apply;

import com.cn.thinkx.ecom.front.api.base.vo.ExpressInfoVO;

import java.util.List;

/**
 * 售后申请返回
 * @author zhuqiuyou
 *
 */
public class OrderRefundApplyResponseVO implements java.io.Serializable {
	
	private static final long serialVersionUID = -2698432173202203373L;

	private String applyId;
	
	private String orderId;
	
	private List<OrderRefundApplySkuResponseVO> applySkuList;
	
	private int returnType;  //退货类型
	
	private int status; // 申请单状态
	
	private  List<ExpressInfoVO> expressInfoList;//退货物流信息
	
	private String denyReason;
	
	private Long createTime;
	
	private Long updateTime;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<OrderRefundApplySkuResponseVO> getApplySkuList() {
		return applySkuList;
	}

	public void setApplySkuList(List<OrderRefundApplySkuResponseVO> applySkuList) {
		this.applySkuList = applySkuList;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<ExpressInfoVO> getExpressInfoList() {
		return expressInfoList;
	}

	public void setExpressInfoList(List<ExpressInfoVO> expressInfoList) {
		this.expressInfoList = expressInfoList;
	}

	public String getDenyReason() {
		return denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
}
