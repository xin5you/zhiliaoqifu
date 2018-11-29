package com.cn.thinkx.ecom.basics.goods.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 商品货品规格对照表
 * 
 * @author zhuqiuyou
 *
 */
public class GoodsSpec extends BaseEntity {

	private static final long serialVersionUID = -9184789738728816327L;

	private String id;
	
	private String specId;  //规格表主键
	
	private String specValueId; //规格值表主键
	
	private String goodsId;  //商品表主键
	
	private String productId;  //货品表主键
	
	
	/**业务数据**/
	private String specName;
	
	private String specValue;
	
	private String specImage;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getSpecValueId() {
		return specValueId;
	}

	public void setSpecValueId(String specValueId) {
		this.specValueId = specValueId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecValue() {
		return specValue;
	}

	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}

	public String getSpecImage() {
		return specImage;
	}

	public void setSpecImage(String specImage) {
		this.specImage = specImage;
	}
}
