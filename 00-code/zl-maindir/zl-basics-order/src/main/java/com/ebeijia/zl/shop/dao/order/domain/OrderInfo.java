package com.ebeijia.zl.shop.dao.order.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class OrderInfo extends Model<OrderInfo> {

    private String memberId;
    private String orderId;
    private String orderPrice;
    private String orderFreightAmt;
    private String payStatus;
    private String payType;
    private String payAmt;
    private String payTime;
    private String sOrderId;
    private String ecomCode;
    private String subOrderStatus;
    private String dmsStatus;
    private String debitAccountCode;
    private String debitAccountType;
    private String debitPrice;

}
