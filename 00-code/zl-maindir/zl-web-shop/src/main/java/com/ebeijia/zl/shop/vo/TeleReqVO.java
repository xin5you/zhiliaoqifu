package com.ebeijia.zl.shop.vo;

/**
 * 话费充值 业务
 *
 * @author zhuqiuyou
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 话费充值返回数据
 *
 * @author zhuqiuyou
 *
 */
@ApiModel("支付信息")
@Data
public class TeleReqVO extends TeleBaseDomain {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty("充值的电话号码")
    private String rechargePhone; // 充值的电话号码

    @ApiModelProperty("充值金额")
    private String rechargeAmount; // 充值金额

    @ApiModelProperty("分销商订单Id")
    private String outerTid; // 分销商订单Id

    @ApiModelProperty("外部分销商回调地址")
    private String callback; // 外部分销商回调地址

    private String productId; // 产品编号

    private String channelOrderId; // 平台订单号

}
