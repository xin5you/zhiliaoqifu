package com.ebeijia.zl.web.api.model.telephone.vo;

import lombok.Data;

/**
 * 立方充值充值状态变更回调
 */
@Data
public class BMRechargeStateChangeVO implements java.io.Serializable{

    private static final long serialVersionUID = 59776366764296476L;

    private String  extData;//	消息扩展字段，JSON格式	String	是	{"name":"value","name1":"value1"}
    private String  rechargeState;//	订单充值状态：1（充值成功） 、9（充值失败）	String	否	1
    private String  outerTid;//	外部订单编号，下单时传递的外部订单编号	String	是	TID2017051245874512
    private String  sign; //	回调签名，仅下单时传递回调地址产生	String	是	444F4A793F22D7483C240FC489D8DB8710D1F45A
    private String  timestamp;//	消息产生时间	String	否	2017-04-13 15:58:41
    private String  userId;//授权的直销商编号	String	否	A891718
    private String  tid; //	平台订单编号，即对应充值订单 Bill 中的 billId 字段	String	否	S1705051216911
}
