package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("收货地址对象")
@Data
public class AddressInfo {

    @ApiModelProperty("收件人")
    String recipient;//

    @ApiModelProperty("手机号")
    String phoneNo;//手机号

    @ApiModelProperty("省")
    String province;//省

    @ApiModelProperty("市")
    String city;//市

    @ApiModelProperty("区县")
    String county;//区县

    @ApiModelProperty("详细地址")
    String address;//详细地址

}
