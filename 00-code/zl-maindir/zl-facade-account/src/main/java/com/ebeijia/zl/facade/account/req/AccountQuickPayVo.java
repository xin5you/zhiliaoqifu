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
public class AccountQuickPayVo implements java.io.Serializable {
	
	private static final long serialVersionUID = -510933645822964156L;

	/**
     * 转入账户类型
     */
    private String tfrInBId;

    /**
     * 转入账户交易金额
     */
    private BigDecimal tfrInAmt;

	/**
	 * 转入账户类型
	 */
	private String tfrOutBId;

	/**
	 * 转入账户交易金额
	 */
	private BigDecimal tfrOutAmt;

	public String getTfrInBId() {
		return tfrInBId;
	}

	public void setTfrInBId(String tfrInBId) {
		this.tfrInBId = tfrInBId;
	}

	public BigDecimal getTfrInAmt() {
		return tfrInAmt;
	}

	public void setTfrInAmt(BigDecimal tfrInAmt) {
		this.tfrInAmt = tfrInAmt;
	}

	public String getTfrOutBId() {
		return tfrOutBId;
	}

	public void setTfrOutBId(String tfrOutBId) {
		this.tfrOutBId = tfrOutBId;
	}

	public BigDecimal getTfrOutAmt() {
		return tfrOutAmt;
	}

	public void setTfrOutAmt(BigDecimal tfrOutAmt) {
		this.tfrOutAmt = tfrOutAmt;
	}
}
