package com.cn.thinkx.ecom.front.api.base.vo.order;

/**
 * 取消订单结果
 * @author zhuqiuyou
 *
 */
public class OrderCancelResult implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5474756038361526766L;

	private int cancelStatus;  //取消状态 0:不允许取消，1:允许取消，2:待审核 
	
	private String rejectReason; //拒绝取消原因

	public int getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(int cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
}
