package com.ebeijia.zl.facade.account.req;

import java.math.BigDecimal;
import java.util.List;

import com.ebeijia.zl.common.utils.req.BaseTxnReq;

/**
 * 
* 
* @Description: 转账交易请求参数
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
public class AccountTransferReqVo extends BaseTxnReq{
	

	private static final long serialVersionUID = 6157195921555421701L;


    /**
     * 实际交易金额
     */
    private BigDecimal transAmt;
 
    /**
     * 上送金额
     */
    private BigDecimal uploadAmt;
    
    
    /**
     * 转入账户
     */
    private String tfrInUserId;
    
    
    /**
     * 转入账户类型
     */
    private String tfrInBId;

    /**
     * 转出账户
     */
    private String tfrOutUserId;
 
    /**
     * 转出账户类型
     */
    private String tfrOutBId;

	/**
	 * 专项账户交易列表
	 */
	private List<AccountTxnVo> transList;
    

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

	public String getTfrInUserId() {
		return tfrInUserId;
	}

	public void setTfrInUserId(String tfrInUserId) {
		this.tfrInUserId = tfrInUserId;
	}

	public String getTfrInBId() {
		return tfrInBId;
	}

	public void setTfrInBId(String tfrInBId) {
		this.tfrInBId = tfrInBId;
	}

	public String getTfrOutUserId() {
		return tfrOutUserId;
	}

	public void setTfrOutUserId(String tfrOutUserId) {
		this.tfrOutUserId = tfrOutUserId;
	}

	public String getTfrOutBId() {
		return tfrOutBId;
	}

	public void setTfrOutBId(String tfrOutBId) {
		this.tfrOutBId = tfrOutBId;
	}

	public List<AccountTxnVo> getTransList() {
		return transList;
	}

	public void setTransList(List<AccountTxnVo> transList) {
		this.transList = transList;
	}
}
