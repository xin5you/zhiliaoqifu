package com.ebeijia.zl.facade.account.req;

import java.util.List;

import com.ebeijia.zl.common.utils.req.BaseTxnReq;

/**
 * 
* 
* @Description: 交易退款请求参数
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
public class AccountRefundReqVo extends BaseTxnReq{
	

	private static final long serialVersionUID = 6157195921555421701L;
    
	/**
	 * 原平台流水号
	 */
	private String orgItfPrimaryKey;
	
	/**
	 * 原渠道订单号
	 */
	private String orgDmsRelatedKey;

	/**
	 * 专项账户交易列表
	 */
	private List<AccountTxnVo> transList;
	
    public String getOrgItfPrimaryKey() {
		return orgItfPrimaryKey;
	}

	public void setOrgItfPrimaryKey(String orgItfPrimaryKey) {
		this.orgItfPrimaryKey = orgItfPrimaryKey;
	}

	public String getOrgDmsRelatedKey() {
		return orgDmsRelatedKey;
	}

	public void setOrgDmsRelatedKey(String orgDmsRelatedKey) {
		this.orgDmsRelatedKey = orgDmsRelatedKey;
	}

	public List<AccountTxnVo> getTransList() {
		return transList;
	}

	public void setTransList(List<AccountTxnVo> transList) {
		this.transList = transList;
	}


	@Override
	public String toString() {
		return "AccountRefundReqVo{" +
				"orgItfPrimaryKey='" + orgItfPrimaryKey + '\'' +
				", orgDmsRelatedKey='" + orgDmsRelatedKey + '\'' +
				", transList=" + transList +
				'}' + super.toString();
	}
}
