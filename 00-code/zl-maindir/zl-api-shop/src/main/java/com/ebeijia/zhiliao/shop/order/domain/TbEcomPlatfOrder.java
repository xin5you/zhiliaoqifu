package com.ebeijia.zhiliao.shop.order.domain;

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
 * 订单总表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_platf_order")
@ApiModel("订单总表")
public class TbEcomPlatfOrder extends Model<TbEcomPlatfOrder> {
 
    @TableId(value = "order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "order_id")
    private String orderId;
 
    @TableField("member_id")
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("dms_related_key")
    @ApiModelProperty(value = "dms_related_key")
    private String dmsRelatedKey;
 
    /**
     * 0：未付款
                        1：已付款待确认
                        2：已付款
                        3：已退款
                        4：部分退款
                        5：部分付款
                        8：已取消
                        9：已完成
     */
    @TableField("pay_status")
    @ApiModelProperty(value = "0：未付款                        1：已付款待确认                        2：已付款                        3：已退款                        4：部分退款                        5：部分付款                        8：已取消                        9：已完成")
    private String payStatus;
 
    /**
     * 单位：分（不含运费）
     */
    @TableField("order_price")
    @ApiModelProperty(value = "单位：分（不含运费）")
    private Integer orderPrice;
 
    /**
     * 单位：分
     */
    @TableField("order_freight_amt")
    @ApiModelProperty(value = "单位：分")
    private Integer orderFreightAmt;
 
    @TableField("pay_type")
    @ApiModelProperty(value = "pay_type")
    private String payType;
 
    @TableField("pay_amt")
    @ApiModelProperty(value = "pay_amt")
    private Integer payAmt;
 
    @TableField("pay_time")
    @ApiModelProperty(value = "pay_time")
    private Long payTime;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("order_channel")
    @ApiModelProperty(value = "order_channel")
    private String orderChannel;
 
    @TableField("col1")
    @ApiModelProperty(value = "col1")
    private String col1;
 
    @TableField("col2")
    @ApiModelProperty(value = "col2")
    private String col2;
 
    @TableField("col3")
    @ApiModelProperty(value = "col3")
    private String col3;


    @Override
    protected Serializable pkVal() { 
        return this.orderId;
    }
}
