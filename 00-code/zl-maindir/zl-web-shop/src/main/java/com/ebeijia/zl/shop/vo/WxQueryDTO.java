package com.ebeijia.zl.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("第四方查询-芸付--返回结果")
public class WxQueryDTO {

    @ApiModelProperty("商户号")
    private String mch_id;

    @ApiModelProperty("商户自己订单号")
    private String out_trade_no;

    @ApiModelProperty("支付金额")
    private Integer total_fee;

    @ApiModelProperty("支付结果")
    private String pay_result;

    @ApiModelProperty("支付时间")
    private String pay_time;

    @ApiModelProperty("订单创建时间")
    private String order_time;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getPay_result() {
        return pay_result;
    }

    public void setPay_result(String pay_result) {
        this.pay_result = pay_result;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

}
