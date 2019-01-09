package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("卡片绑定对象")
@Data
public class CardBindInfo {

    @ApiModelProperty("持卡人")
    String userName;//

    @ApiModelProperty("手机号")
    String phoneNo;//手机号

    @ApiModelProperty("省")
    String province;//省

    @ApiModelProperty("市")
    String city;//市

    @ApiModelProperty("区县")
    String county;//区县

    @ApiModelProperty("开户支行")
    String bankDetail;//

    @ApiModelProperty("卡号")
    String cardNumber;//

    @ApiModelProperty("身份证号")
    String idCard;

}
