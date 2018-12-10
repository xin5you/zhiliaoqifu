package com.cn.thinkx.ecom.front.api.base.vo.goods;

public class APIItemSkuSpecValueTO {
	private APISkuSpecTO skuSpec;
	private APISkuSpecValueTO skuSpecValue;
	public APISkuSpecTO getSkuSpec() {
		return skuSpec;
	}
	public void setSkuSpec(APISkuSpecTO skuSpec) {
		this.skuSpec = skuSpec;
	}
	public APISkuSpecValueTO getSkuSpecValue() {
		return skuSpecValue;
	}
	public void setSkuSpecValue(APISkuSpecValueTO skuSpecValue) {
		this.skuSpecValue = skuSpecValue;
	}
}
