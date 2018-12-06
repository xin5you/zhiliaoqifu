package com.ebeijia.zl.facade.account.req;

import java.math.BigDecimal;

/**
 * 
* 
* @Description: 交易的账户类型和交易类型
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
public class AccountTxnVo implements java.io.Serializable {
	
	private static final long serialVersionUID = -510933645822964156L;

	/**
     * 账户类型
     */
    private String txnBId;
    
    
    /**
     * 交易金额
     */
    private BigDecimal txnAmt;
    

	public String getTxnBId() {
		return txnBId;
	}

	public void setTxnBId(String txnBId) {
		this.txnBId = txnBId;
	}

	public BigDecimal getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(BigDecimal txnAmt) {
		this.txnAmt = txnAmt;
	}
  
}
