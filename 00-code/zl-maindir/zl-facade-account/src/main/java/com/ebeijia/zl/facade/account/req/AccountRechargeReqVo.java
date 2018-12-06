package com.ebeijia.zl.facade.account.req;

import java.math.BigDecimal;
import java.util.List;

import com.ebeijia.zl.common.utils.req.BaseTxnReq;

/**
 * 
* 
* @Description: 账户充值请求参数
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
public class AccountRechargeReqVo extends BaseTxnReq{
	

	private static final long serialVersionUID = 6157195921555421701L;


	/**
	 * 手机号
	 */
	private String mobilePhone;
	
	/**
	 * 企业信息
	 */
	private String fromCompanyId;
	
	/**
	 * 账户信息
	 */
	private String priBId;
	

    /**
     * 实际交易金额
     */
    private BigDecimal transAmt;
 
    /**
     * 上送金额
     */
    private BigDecimal uploadAmt;
    
    
    /**
     * 专项账户交易列表
     */
    private List<AccountTxnVo> transList;
    

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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


	public List<AccountTxnVo> getTransList() {
		return transList;
	}

	public void setTransList(List<AccountTxnVo> transList) {
		this.transList = transList;
	}

	public String getFromCompanyId() {
		return fromCompanyId;
	}

	public void setFromCompanyId(String fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}

	public String getPriBId() {
		return priBId;
	}

	public void setPriBId(String priBId) {
		this.priBId = priBId;
	}
	
}
