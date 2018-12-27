package com.ebeijia.zl.shop.dao.order.domain;

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
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_platf_order")
@ApiModel("订单总表")
public class TbEcomPlatfOrder extends Model<TbEcomPlatfOrder> {
 
    @TableField("col1")
    @ApiModelProperty(value = "col1")
    private String col1;
 
    @TableField("col2")
    @ApiModelProperty(value = "col2")
    private String col2;
 
    @TableField("col3")
    @ApiModelProperty(value = "col3")
    private String col3;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    /**
     * 关联BID用
     */
    @TableField("dms_related_key")
    @ApiModelProperty(value = "关联BID用")
    private String dmsRelatedKey;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 会员ID
     */
    @TableField("member_id")
    @ApiModelProperty(value = "会员ID")
    private String memberId;
 
    @TableField("order_channel")
    @ApiModelProperty(value = "order_channel")
    private String orderChannel;
 
    /**
     * 配送总费用 单位：分
     */
    @TableField("order_freight_amt")
    @ApiModelProperty(value = "配送总费用 单位：分")
    private Long orderFreightAmt;
 
    /**
     * 主订单ID
     */
    @TableId(value = "order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "主订单ID")
    private String orderId;
 
    /**
     * 订单金额 单位：分（不含运费）
     */
    @TableField("order_price")
    @ApiModelProperty(value = "订单金额 单位：分（不含运费）")
    private Long orderPrice;
 
    /**
     * 支付金额
     */
    @TableField("pay_amt")
    @ApiModelProperty(value = "支付金额")
    private Long payAmt;
 
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
 
    @TableField("pay_time")
    @ApiModelProperty(value = "pay_time")
    private Long payTime;
 
    /**
     * 支付类型
     */
    @TableField("pay_type")
    @ApiModelProperty(value = "支付类型")
    private String payType;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;

    @TableField(exist = false)
    private String mobilePhoneNo;

    @TableField(exist = false)
    private String personalName;

    @Override
    protected Serializable pkVal() { 
        return this.orderId;
    }
}
