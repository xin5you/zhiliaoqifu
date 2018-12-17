package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ApiModel("支付信息")
@Getter
@Setter
@ToString
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("A类账户类型,目前已有通卡账户和托管账户")
    String typeA;

    @ApiModelProperty("B类账户类型，对应多个专项账户类型之一")
    String typeB;

    @ApiModelProperty("单位分，从A类账户扣款金额")
    Long costA;

    @ApiModelProperty("单位分，从B类账户扣款金额")
    Long costB;
}
