package com.ebeijia.zl.core.withdraw.suning.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "withdraw.yfb")
public class YFBWithdrawConfig {

    private String publicKeyIndex;

    private String wagKeyString;

    private String merchantNo;

    private String productCode;

    private String goodsType;

    private String privateKeyUrl;

    private String bathWithdrawUrl;

    private String yfbNotifyUrl;

    public String getPublicKeyIndex() {
        return publicKeyIndex;
    }
    public String getWagKeyString() {
        return wagKeyString;
    }
    public void setWagKeyString(String wagKeyString) {
        this.wagKeyString = wagKeyString;
    }

    public void setPublicKeyIndex(String publicKeyIndex) {
        this.publicKeyIndex = publicKeyIndex;

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

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getPrivateKeyUrl() {
        return privateKeyUrl;
    }

    public void setPrivateKeyUrl(String privateKeyUrl) {
        this.privateKeyUrl = privateKeyUrl;
    }

    public String getBathWithdrawUrl() {
        return bathWithdrawUrl;
    }

    public void setBathWithdrawUrl(String bathWithdrawUrl) {
        this.bathWithdrawUrl = bathWithdrawUrl;
    }

    public String getYfbNotifyUrl() {
        return yfbNotifyUrl;
    }

    public void setYfbNotifyUrl(String yfbNotifyUrl) {
        this.yfbNotifyUrl = yfbNotifyUrl;
    }
}
