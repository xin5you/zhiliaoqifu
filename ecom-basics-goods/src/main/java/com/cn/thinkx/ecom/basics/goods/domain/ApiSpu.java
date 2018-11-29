package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 *  渠道商品SPU
 * @author zhuqiuyou
 *
 */
public class ApiSpu extends BaseEntity {

	private static final long serialVersionUID = -1284966870371001413L;
	
	private String id;
	
	private String 	ecomCode; //商城渠道
	
	private String spuCode; //商城spu
	
	private String skuSyncStat;
	
	private String ecomType;
	private String skuSyncType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEcomCode() {
		return ecomCode;
	}
	public void setEcomCode(String ecomCode) {
		this.ecomCode = ecomCode;
	}
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getSkuSyncStat() {
		return skuSyncStat;
	}
	public void setSkuSyncStat(String skuSyncStat) {
		this.skuSyncStat = skuSyncStat;
	}
	public String getEcomType() {
		return ecomType;
	}
	public void setEcomType(String ecomType) {
		this.ecomType = ecomType;
	}
	public String getSkuSyncType() {
		return skuSyncType;
	}
	public void setSkuSyncType(String skuSyncType) {
		this.skuSyncType = skuSyncType;
	}
	@Override
	public String toString() {
		return "ApiSpu [id=" + id + ", ecomCode=" + ecomCode + ", spuCode=" + spuCode + ", skuSyncStat=" + skuSyncStat
				+ "]";
	}
}
