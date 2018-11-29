package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 退款表
 * 
 * @author xiaomei
 *
 */
public class OrderRefund extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;

	private String refundId;
	private String returnsId; // 申请售后ID
	private String sOrderId; // 订单ID
	private String dmsRelatedKey; // 退货流水号
	private String refundAmt; // 退款金额
	private String memberId; // 会员id
	private String refundStatus; // 退款状态
	private String refundTime; // 退款时间

	public String getRefundId() {
		return refundId;
	}

	public String getReturnsId() {
		return returnsId;
	}

	public String getsOrderId() {
		return sOrderId;
	}

	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}

	public String getRefundAmt() {
		return refundAmt;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public void setReturnsId(String returnsId) {
		this.returnsId = returnsId;
	}

	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}

	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}

	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	@Override
	public String toString() {
		return "OrderRefund [refundId=" + refundId + ", returnsId=" + returnsId + ", sOrderId=" + sOrderId
				+ ",dmsRelatedKey=" + dmsRelatedKey + ", refundAmt=" + refundAmt + ", memberId=" + memberId
				+ ", refundStatus=" + refundStatus + ", refundTime=" + refundTime + "]";
	}

}
