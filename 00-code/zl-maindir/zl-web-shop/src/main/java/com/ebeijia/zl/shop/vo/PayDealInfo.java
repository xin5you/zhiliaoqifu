package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.dao.order.domain.TbEcomPayOrderDetails;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PayDealInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    String outId;

    String priBid;

    String dealType;

    String payStatus;

    String time;

    List<TbEcomPayOrderDetails> payDetail;

}
