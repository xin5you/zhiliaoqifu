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
 * 渠道支付明细表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_pay_order_details")
@ApiModel("渠道支付明细表")
public class TbEcomPayOrderDetails extends Model<TbEcomPayOrderDetails> {
 
    @TableId(value = "pay_details_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "pay_details_id")
    private String payDetailsId;
 
    @TableField("pay_order_id")
    @ApiModelProperty(value = "pay_order_id")
    private String payOrderId;
 
    @TableField("out_order_id")
    @ApiModelProperty(value = "out_order_id")
    private String outOrderId;
 
    @TableField("dms_related_key")
    @ApiModelProperty(value = "dms_related_key")
    private String dmsRelatedKey;
 
    /**
     * 0：未支付
                        1：支付失败
                        2：支付成功
                        3：部分退款
                        4：退款成功
     */
    @TableField("pay_status")
    @ApiModelProperty(value = "0：未支付                        1：支付失败                        2：支付成功                        3：部分退款                        4：退款成功")
    private String payStatus;
 
    @TableField("debit_account_code")
    @ApiModelProperty(value = "debit_account_code")
    private String debitAccountCode;
 
    /**
     * 00:A类型账户
                        01:B类型账户
     */
    @TableField("debit_account_type")
    @ApiModelProperty(value = "00:A类型账户                        01:B类型账户")
    private String debitAccountType;
 
    /**
     * 单位：分
     */
    @TableField("debit_price")
    @ApiModelProperty(value = "单位：分")
    private Integer debitPrice;
 
    @TableField("pay_time")
    @ApiModelProperty(value = "pay_time")
    private Long payTime;
 
    @TableField("debit_desc")
    @ApiModelProperty(value = "debit_desc")
    private String debitDesc;
 
    @TableField("resv1")
    @ApiModelProperty(value = "resv1")
    private String resv1;
 
    @TableField("resv2")
    @ApiModelProperty(value = "resv2")
    private String resv2;
 
    @TableField("resv3")
    @ApiModelProperty(value = "resv3")
    private String resv3;
 
    @TableField("resv4")
    @ApiModelProperty(value = "resv4")
    private String resv4;
 
    @TableField("resv5")
    @ApiModelProperty(value = "resv5")
    private String resv5;
 
    @TableField("resv6")
    @ApiModelProperty(value = "resv6")
    private String resv6;
 
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


    @Override
    protected Serializable pkVal() { 
        return this.payDetailsId;
    }
}
