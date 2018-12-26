package com.ebeijia.zl.core.withdraw.suning.vo;

import java.util.List;

public class WithdrawBodyVO implements java.io.Serializable{


    private static final long serialVersionUID = -2057174030649972247L;

    private String batchNo;   //批量付款批次号
    private String merchantNo; //付款方商户号(该字段和批次号 的组合应保证唯一)
    private String productCode;  //产品编码(易付宝分配)
    private int totalNum;  //批量付款总笔数（至少1笔）
    private Long totalAmount; //1~10000000000000。单位：分
    private String currency; //币种编码，目前必须传递”CNY”
    private String payDate; //支付时间。格式:yyyyMMdd
    private String tunnelData;  //业务扩展字段
    private String remark;
    private List<WithdrawDetailDataVO> detailData;
    private String notifyUrl;        //易付宝服务器主动通知商户网站 里指定的页面URL路径
    private String batchOrderName;  //批次订单名称（最大长度256字 节）
    private String goodsType;      //商品类型编码（最大长度8字 节）

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getTunnelData() {
        return tunnelData;
    }

    public void setTunnelData(String tunnelData) {
        this.tunnelData = tunnelData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<WithdrawDetailDataVO> getDetailData() {
        return detailData;
    }

    public void setDetailData(List<WithdrawDetailDataVO> detailData) {
        this.detailData = detailData;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBatchOrderName() {
        return batchOrderName;
    }

    public void setBatchOrderName(String batchOrderName) {
        this.batchOrderName = batchOrderName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
