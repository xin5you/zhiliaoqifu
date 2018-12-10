package com.cn.thinkx.ecom.front.api.base.vo;

/**
 * 严选系统中SKU库存信息
 * @author zhuqiuyou
 *
 */
public class ChannelSkuInventoryOutVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8209257647353385400L;
	
	private String skuId;
	
	private String inventory;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

}
