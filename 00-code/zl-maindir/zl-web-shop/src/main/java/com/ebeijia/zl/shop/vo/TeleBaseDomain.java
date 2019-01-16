package com.ebeijia.zl.shop.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统参数信息")
@Data
public class TeleBaseDomain implements java.io.Serializable {

    @ApiModelProperty("渠道标识")
    private String channelId; //渠道标识

    @ApiModelProperty("渠道token")
    private String channelToken; //渠道token

    @ApiModelProperty("充值的类型")
    private String method;//充值的类型

    @ApiModelProperty("版本")
    private String v; //版本

    @ApiModelProperty("日期格式")
    private String timestamp; //日期格式

    @ApiModelProperty("签名")
    private String sign; //系统签名
}
