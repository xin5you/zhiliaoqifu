package com.ebeijia.zl.facade.account.req;

import java.math.BigDecimal;
import java.util.List;

import com.ebeijia.zl.common.utils.req.BaseTxnReq;

/**
 * 
* 
* @Description: 账户消费请求参数
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
public class AccountConsumeReqVo extends BaseTxnReq{
	

	private static final long serialVersionUID = 6157195921555421701L;

	private String priBId;

    /**
     * 实际交易金额
     */
    private BigDecimal transAmt; //必须填写 当前支付总金额
 
    /**
     * 上送金额
     */
    private BigDecimal uploadAmt; //必须填写

    
    /**
     * 专项账户交易列表
     */
    private List<AccountTxnVo> transList;  //必须填写 专项支付的明细项

	/**
	 * 消费补充列表（第三方充值到专项）
	 */
	private List<AccountQuickPayVo> addList; //快捷支付填写

	private String mchntCode; //商户

	private String shopCode; //门店

	public String getPriBId() {
		return priBId;
	}

	public void setPriBId(String priBId) {
		this.priBId = priBId;
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

	public List<AccountTxnVo> getTransList() {
		return transList;
	}

	public void setTransList(List<AccountTxnVo> transList) {
		this.transList = transList;
	}

	public List<AccountQuickPayVo> getAddList() {
		return addList;
	}

	public void setAddList(List<AccountQuickPayVo> addList) {
		this.addList = addList;
	}

	@Override
	public String toString() {
		return "AccountConsumeReqVo{" +
				"priBId='" + priBId + '\'' +
				", transAmt=" + transAmt +
				", uploadAmt=" + uploadAmt +
				", transList=" + transList +
				", addList=" + addList +
				", mchntCode='" + mchntCode + '\'' +
				", shopCode='" + shopCode + '\'' +
				"} " + super.toString();
	}
}
