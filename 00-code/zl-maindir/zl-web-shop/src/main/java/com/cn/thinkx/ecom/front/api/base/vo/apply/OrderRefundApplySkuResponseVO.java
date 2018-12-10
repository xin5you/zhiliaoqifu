package com.cn.thinkx.ecom.front.api.base.vo.apply;

import java.math.BigDecimal;
import java.util.List;

/**
 * 申请售后的sku信息
 * @author zhuqiuyou
 *
 */
public class OrderRefundApplySkuResponseVO implements java.io.Serializable {

	private static final long serialVersionUID = -8125376317876805430L;

	private String skuId;  //申请售后的skuId
	
	private Integer count;  //申请售后的sku数量
	
	private ApplySkuReason applySkuReason;
	
	private List<ApplyPicVO> applyPicList;  //申请售后的图片信息
	
	private BigDecimal originalPrice; //sku单价
	
	private BigDecimal  subtotalPrice;//sku实付价格
	
	private List<OperateSkuVO> operateSkus;
	
	private OrderRefundApplySkuAddressVO returnStoreHouse;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ApplySkuReason getApplySkuReason() {
		return applySkuReason;
	}

	public void setApplySkuReason(ApplySkuReason applySkuReason) {
		this.applySkuReason = applySkuReason;
	}

	public List<ApplyPicVO> getApplyPicList() {
		return applyPicList;
	}

	public void setApplyPicList(List<ApplyPicVO> applyPicList) {
		this.applyPicList = applyPicList;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getSubtotalPrice() {
		return subtotalPrice;
	}

	public void setSubtotalPrice(BigDecimal subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
	}

	public List<OperateSkuVO> getOperateSkus() {
		return operateSkus;
	}

	public void setOperateSkus(List<OperateSkuVO> operateSkus) {
		this.operateSkus = operateSkus;
	}

	public OrderRefundApplySkuAddressVO getReturnStoreHouse() {
		return returnStoreHouse;
	}

	public void setReturnStoreHouse(OrderRefundApplySkuAddressVO returnStoreHouse) {
		this.returnStoreHouse = returnStoreHouse;
	}
	
}
