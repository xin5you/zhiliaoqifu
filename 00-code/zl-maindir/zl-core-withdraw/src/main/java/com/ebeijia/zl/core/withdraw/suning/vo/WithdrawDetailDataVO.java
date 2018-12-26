package com.ebeijia.zl.core.withdraw.suning.vo;

public class WithdrawDetailDataVO implements java.io.Serializable {

    private static final long serialVersionUID = 5660178049318585737L;

    private String serialNo;

    private String receiverType="PERSON";

    private String receiverCurrency="CNY";

    private String receiverName;

    private Long amount;

    private String orderName;

    private String bankName;

    private String receiverCardNo;

    private String bankCode;

    private String bankProvince;

    private String bankCity;

    private String payeeBankLinesNo;

    private String remark;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverCurrency() {
        return receiverCurrency;
    }

    public void setReceiverCurrency(String receiverCurrency) {
        this.receiverCurrency = receiverCurrency;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReceiverCardNo() {
        return receiverCardNo;
    }

    public void setReceiverCardNo(String receiverCardNo) {
        this.receiverCardNo = receiverCardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getPayeeBankLinesNo() {
        return payeeBankLinesNo;
    }

    public void setPayeeBankLinesNo(String payeeBankLinesNo) {
        this.payeeBankLinesNo = payeeBankLinesNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
