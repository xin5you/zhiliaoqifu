package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("转账信息")
@Getter
@Setter
@ToString
public class DealInfo {

    @ApiModelProperty("源账户类型，目前只有托管账户可以转账")
    String sourceType;

    @ApiModelProperty("目标账户类型")
    String targetType;

    @ApiModelProperty("源账户ID")
    String sourceAccountId;

    @ApiModelProperty("目标账户ID")
    String targetAccountId;

    @ApiModelProperty("单位分，转账金额")
    Long amount;
}
