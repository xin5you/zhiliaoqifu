package com.cn.thinkx.ecom.basics.order.domain;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.cn.thinkx.ecom.common.domain.BaseEntity;

/**
 * 订单退换货申请表
 * 
 * @author xiaomei
 *
 */
public class ReturnsOrder extends BaseEntity {

	private static final long serialVersionUID = -7655982014086839784L;
	
	private String returnsId;	
	private String sOrderId;	//订单编号
	private String memberId;	//会员ID
	private String returnsStatus;	//审核状态
	private String returnsType;	//退换货类型	
	private String addTime;	//申请时间
	private String photoImg;	//货物签收图片
	private String refuseReason;	//拒绝理由
	private String applyReasonType;
	private String applyReason;	//原因说明
	private String applyId; //申请单id
	private String resv1;
	private String resv2;
	private String resv3;
	private String resv4;
	private String resv5;
	private String resv6;
	
	private String mobilePhoneNo;//会员手机号
	private byte[] nickname;// 昵称,二进制保存emoji表情
	private String nicknameStr;// 昵称显示
	private String dmsRelatedKey;//退款流水号
	private String refundAmt;//退款金额
	private String refundStatus;//退款状态
	private String refundTime;//退款时间
	private String refundId;//退款id
	private String retStatType;
	private String retType;
	private String refStatType;
	
	private String oItemId;
	private String goodsId;
	private String productPrice;
	private String productNum;
	private String productName;
	private String picUrl;
	private String applyReturnState;
	private String applyReturnType;
	
	public String getReturnsId() {
		return returnsId;
	}
	public void setReturnsId(String returnsId) {
		this.returnsId = returnsId;
	}
	public String getsOrderId() {
		return sOrderId;
	}
	public void setsOrderId(String sOrderId) {
		this.sOrderId = sOrderId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getReturnsStatus() {
		return returnsStatus;
	}
	public void setReturnsStatus(String returnsStatus) {
		this.returnsStatus = returnsStatus;
	}
	public String getReturnsType() {
		return returnsType;
	}
	public void setReturnsType(String returnsType) {
		this.returnsType = returnsType;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getPhotoImg() {
		return photoImg;
	}
	public void setPhotoImg(String photoImg) {
		this.photoImg = photoImg;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}
	public byte[] getNickname() {
		return nickname;
	}
	public void setNickname(byte[] nickname) {
		this.nickname = nickname;
	}
	public String getNicknameStr() {
		if (this.getNickname() != null) {
			try {
				this.nicknameStr = new String(this.getNickname(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return nicknameStr;
	}
	public void setNicknameStr(String nicknameStr) {
		this.nicknameStr = nicknameStr;
	}
	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}
	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}
	public String getRefundAmt() {
		return refundAmt;
	}
	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	public String getRetStatType() {
		return retStatType;
	}
	public void setRetStatType(String retStatType) {
		this.retStatType = retStatType;
	}
	public String getRetType() {
		return retType;
	}
	public void setRetType(String retType) {
		this.retType = retType;
	}
	public String getRefStatType() {
		return refStatType;
	}
	public void setRefStatType(String refStatType) {
		this.refStatType = refStatType;
	}
	public String getApplyReasonType() {
		return applyReasonType;
	}
	public void setApplyReasonType(String applyReasonType) {
		this.applyReasonType = applyReasonType;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getResv1() {
		return resv1;
	}
	public void setResv1(String resv1) {
		this.resv1 = resv1;
	}
	public String getResv2() {
		return resv2;
	}
	public void setResv2(String resv2) {
		this.resv2 = resv2;
	}
	public String getResv3() {
		return resv3;
	}
	public void setResv3(String resv3) {
		this.resv3 = resv3;
	}
	public String getResv4() {
		return resv4;
	}
	public void setResv4(String resv4) {
		this.resv4 = resv4;
	}
	public String getResv5() {
		return resv5;
	}
	public void setResv5(String resv5) {
		this.resv5 = resv5;
	}
	public String getResv6() {
		return resv6;
	}
	public void setResv6(String resv6) {
		this.resv6 = resv6;
	}
	
	public String getoItemId() {
		return oItemId;
	}
	public void setoItemId(String oItemId) {
		this.oItemId = oItemId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getApplyReturnState() {
		return applyReturnState;
	}
	public void setApplyReturnState(String applyReturnState) {
		this.applyReturnState = applyReturnState;
	}
	public String getApplyReturnType() {
		return applyReturnType;
	}
	public void setApplyReturnType(String applyReturnType) {
		this.applyReturnType = applyReturnType;
	}
	@Override
	public String toString() {
		return "ReturnsOrder [returnsId=" + returnsId + ", sOrderId=" + sOrderId + ", memberId=" + memberId
				+ ", returnsStatus=" + returnsStatus + ", returnsType=" + returnsType + ", addTime=" + addTime
				+ ", photoImg=" + photoImg + ", refuseReason=" + refuseReason + ", applyReasonType=" + applyReasonType
				+ ", applyReason=" + applyReason + ", applyId=" + applyId + ", resv1=" + resv1 + ", resv2=" + resv2
				+ ", resv3=" + resv3 + ", resv4=" + resv4 + ", resv5=" + resv5 + ", resv6=" + resv6 + ", mobilePhoneNo="
				+ mobilePhoneNo + ", nickname=" + Arrays.toString(nickname) + ", nicknameStr=" + nicknameStr
				+ ", dmsRelatedKey=" + dmsRelatedKey + ", refundAmt=" + refundAmt + ", refundStatus=" + refundStatus
				+ ", refundTime=" + refundTime + ", refundId=" + refundId + ", retStatType=" + retStatType
				+ ", retType=" + retType + ", refStatType=" + refStatType + "]";
	}
	
}
