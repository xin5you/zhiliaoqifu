package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class WxPaySuccessDTO {

    @ApiModelProperty("商户号")
    private String merid;


    @ApiModelProperty("商户订单号")
    @NotBlank
    private String merchantOutOrderNo;

    @ApiModelProperty("支付方式")
    private String payType;


    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }

    public String getMerchantOutOrderNo() {
        return merchantOutOrderNo;
    }

    public void setMerchantOutOrderNo(String merchantOutOrderNo) {
        this.merchantOutOrderNo = merchantOutOrderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "WxPaySuccessDTO{" +
                "merid='" + merid + '\'' +
                ", merchantOutOrderNo='" + merchantOutOrderNo + '\'' +
                ", payType='" + payType + '\'' +
                '}';
    }
}
