package com.ebeijia.zl.shop.core.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * 订单信息表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_inf")
@ApiModel("订单信息表")
public class TbEcomOrderInf extends Model<TbEcomOrderInf> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("channel")
    @ApiModelProperty(value = "channel")
    private String channel;
 
    @TableField("user_id")
    @ApiModelProperty(value = "user_id")
    private String userId;
 
    @TableField("router_order_no")
    @ApiModelProperty(value = "router_order_no")
    private String routerOrderNo;
 
    @TableField("merchant_no")
    @ApiModelProperty(value = "merchant_no")
    private String merchantNo;
 
    @TableField("shop_no")
    @ApiModelProperty(value = "shop_no")
    private String shopNo;
 
    @TableField("commodity_name")
    @ApiModelProperty(value = "commodity_name")
    private String commodityName;
 
    @TableField("commodity_num")
    @ApiModelProperty(value = "commodity_num")
    private String commodityNum;
 
    @TableField("txn_amount")
    @ApiModelProperty(value = "txn_amount")
    private String txnAmount;
 
    @TableField("order_type")
    @ApiModelProperty(value = "order_type")
    private String orderType;
 
    @TableField("notify_url")
    @ApiModelProperty(value = "notify_url")
    private String notifyUrl;
 
    @TableField("redirect_url")
    @ApiModelProperty(value = "redirect_url")
    private String redirectUrl;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
