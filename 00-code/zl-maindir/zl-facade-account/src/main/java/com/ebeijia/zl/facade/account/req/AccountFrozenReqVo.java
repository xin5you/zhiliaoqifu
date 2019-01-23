package com.ebeijia.zl.facade.account.req;

import com.ebeijia.zl.common.utils.req.BaseTxnReq;

import java.math.BigDecimal;
import java.util.List;

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
public class AccountFrozenReqVo extends BaseTxnReq{

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

	private String orgItfPrimaryKey;//原交易流水Id

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

	public String getOrgItfPrimaryKey() {
		return orgItfPrimaryKey;
	}

	public void setOrgItfPrimaryKey(String orgItfPrimaryKey) {
		this.orgItfPrimaryKey = orgItfPrimaryKey;
	}
}
