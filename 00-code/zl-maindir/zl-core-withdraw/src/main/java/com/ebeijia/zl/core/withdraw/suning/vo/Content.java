package com.ebeijia.zl.core.withdraw.suning.vo;

import java.util.List;

/**
 * 代付回调参数
 * 
 * @author zhuqi
 *
 */
public class Content implements java.io.Serializable {

	private static final long serialVersionUID = -3893380687361465510L;

	private String batchNo;		//批次号
	private String merchantNo;	//付款方商户号
	private String dataSource;	//数据源
	private Integer totalNum;		//总笔数
	private Long totalAmount;	//支付总金额
	private Integer successNum;		//成功笔数
	private Long successAmount;	//成功总金额
	private Integer failNum;		//失败笔数
	private Long failAmount;	//失败金额
	private Long poundage;		//总手续费
	private List<TransferOrders> transferOrders;	//批次明细
	private String status;		//状态
	private String errorCode;	//错误码
	private String errorMsg;	//错误原因
	private String payMerchantno;	//付款方商户号

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}

	public Long getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(Long successAmount) {
		this.successAmount = successAmount;
	}

	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}

	public Long getFailAmount() {
		return failAmount;
	}

	public void setFailAmount(Long failAmount) {
		this.failAmount = failAmount;
	}

	public Long getPoundage() {
		return poundage;
	}

	public void setPoundage(Long poundage) {
		this.poundage = poundage;
	}

	public List<TransferOrders> getTransferOrders() {
		return transferOrders;
	}

	public void setTransferOrders(List<TransferOrders> transferOrders) {
		this.transferOrders = transferOrders;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getPayMerchantno() {
		return payMerchantno;
	}

	public void setPayMerchantno(String payMerchantno) {
		this.payMerchantno = payMerchantno;
	}
}
