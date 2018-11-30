package com.cn.thinkx.ecom.front.api.base.vo.order;

import java.util.List;

/**
 * 物流详细信息
 * @author zhuqiuyou
 *
 */

public class ExpressDetailInfoVO implements java.io.Serializable {
	
	private static final long serialVersionUID = -6380615928892781956L;

	private String expressCompany;
	
	private String expressNo;
	
	private List<String> subExpressNos; //子物流单列表
	
	private List<SkuVO> skus;

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public List<String> getSubExpressNos() {
		return subExpressNos;
	}

	public void setSubExpressNos(List<String> subExpressNos) {
		this.subExpressNos = subExpressNos;
	}

	public List<SkuVO> getSkus() {
		return skus;
	}

	public void setSkus(List<SkuVO> skus) {
		this.skus = skus;
	}
}
