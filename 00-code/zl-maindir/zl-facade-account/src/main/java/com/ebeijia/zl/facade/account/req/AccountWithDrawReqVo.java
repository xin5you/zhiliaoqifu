package com.ebeijia.zl.facade.account.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ebeijia.zl.common.utils.req.BaseTxnReq;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
* 
* @Description: 账户提现请求
*
* @version: v1.0.0
* @author: zhuqi
* @date: 2018年12月5日 下午12:52:30 
*
* Modification History:
* Date         Author          Version
*-------------------------------------*
* 2018年12月5日     zhuqi           v1.0.0
 */
public class AccountWithDrawReqVo extends BaseTxnReq{

	private static final long serialVersionUID = 7273421483520332385L;

	private String mchntCode; //商户

	private String shopCode; //门店

	/**
     * 实际交易金额 单位分
     */
    private BigDecimal transAmt; //必须填写 当前支付总金额
 
    /**
     * 上送金额 单位分
     */
    private BigDecimal uploadAmt; //必须填写


	/**
	 * 收款方姓名 必填
	 */
	private String receiverName;

	/**
	 * 收款方卡号 必填
	 */
	private String receiverCardNo;

	/**
	 * 收款方类型（PERSON：个人，CORP：企业）
	 */
	private String receiverType;

	/**
	 * 开户行名称 必填
	 */
	private String bankName;

	/**
	 * 开户行CODE 必填
	 */
	private String bankCode;

	/**
	 * 开户行省
	 */
	private String bankProvince;

	/**
	 * 开户城市
	 */
	private String bankCity;

	/**
	 * 订单名称
	 */
	private String orderName;

	/**
	 * 订单备注
	 */
	private String remarks;

	public String getMchntCode() {
		return mchntCode;
	}

	public void setMchntCode(String mchntCode) {
		this.mchntCode = mchntCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public BigDecimal getUploadAmt() {
		return uploadAmt;
	}

	public void setUploadAmt(BigDecimal uploadAmt) {
		this.uploadAmt = uploadAmt;
	}

	public String getReceiverCardNo() {
		return receiverCardNo;
	}

	public void setReceiverCardNo(String receiverCardNo) {
		this.receiverCardNo = receiverCardNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
