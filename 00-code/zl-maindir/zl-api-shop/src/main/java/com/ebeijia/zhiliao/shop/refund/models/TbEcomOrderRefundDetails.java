package com.ebeijia.zhiliao.shop.refund.models;

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
 * 渠道退款明细
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_refund_details")
@ApiModel("渠道退款明细")
public class TbEcomOrderRefundDetails extends Model<TbEcomOrderRefundDetails> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("dms_return_no")
    @ApiModelProperty(value = "dms_return_no")
    private String dmsReturnNo;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("out_returns_id")
    @ApiModelProperty(value = "out_returns_id")
    private String outReturnsId;
 
    @TableField("pay_details_id")
    @ApiModelProperty(value = "pay_details_id")
    private String payDetailsId;
 
    @TableField("refund_account_code")
    @ApiModelProperty(value = "refund_account_code")
    private String refundAccountCode;
 
    /**
     * 00:A类型账户
                        01:B类型账户
     */
    @TableField("refund_account_type")
    @ApiModelProperty(value = "00:A类型账户                        01:B类型账户")
    private String refundAccountType;
 
    /**
     * 单位：分
     */
    @TableField("refund_amt")
    @ApiModelProperty(value = "单位：分")
    private Integer refundAmt;
 
    @TableId(value = "refund_details_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "refund_details_id")
    private String refundDetailsId;
 
    /**
     * 0：申请中
                        1：已退款
                        2：退款失败
     */
    @TableField("refund_status")
    @ApiModelProperty(value = "0：申请中                        1：已退款                        2：退款失败")
    private String refundStatus;
 
    @TableField("refund_time")
    @ApiModelProperty(value = "refund_time")
    private Long refundTime;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
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
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.refundDetailsId;
    }
}
