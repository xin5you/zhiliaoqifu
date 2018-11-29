package com.cn.thinkx.ecom.basics.order.domain;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 退货商品表
 * 
 * @author xiaomei
 *
 */
public class SellbackGoodslist extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	
	private String id; 
	private String sOrderId;	//门店订单ID
	private String returnsId;	//退货申请ID
	private String oItemId;		//订单明细ID
	private String shipNum;		//发货数量
	private String returnNum;	//退货数量
	private String storageNum;	//入库数量
	private String applyReturnState;	//退货状态
	private String goodsRemark;	//退货商品备注
	public String getId() {
		return id;
	}
	public String getsOrderId() {
		return sOrderId;
	}
	public String getReturnsId() {
		return returnsId;
	}
	public String getoItemId() {
		return oItemId;
	}
	public String getShipNum() {
		return shipNum;
	}
	public String getReturnNum() {
		return returnNum;
	}
	public String getStorageNum() {
		return storageNum;
	}
	public String getApplyReturnState() {
		return applyReturnState;
	}
	public String getGoodsRemark() {
		return goodsRemark;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}
	public void setReturnsId(String returnsId) {
		this.returnsId = returnsId;
	}
	public void setoItemId(String oItemId) {
		this.oItemId = oItemId;
	}
	public void setShipNum(String shipNum) {
		this.shipNum = shipNum;
	}
	public void setReturnNum(String returnNum) {
		this.returnNum = returnNum;
	}
	public void setStorageNum(String storageNum) {
		this.storageNum = storageNum;
	}
	public void setApplyReturnState(String applyReturnState) {
		this.applyReturnState = applyReturnState;
	}
	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}
	@Override
	public String toString() {
		return "SellbackGoodslist [id=" + id + ", sOrderId=" + sOrderId + ", returnsId=" + returnsId + ", oItemId="
				+ oItemId + ", shipNum=" + shipNum + ", returnNum=" + returnNum + ", storageNum=" + storageNum
				+ ", applyReturnState=" + applyReturnState + ", goodsRemark=" + goodsRemark + "]";
	}
	
}
