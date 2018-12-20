package com.ebeijia.zl.web.oms.inaccount.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * tb_inaccount_order
 *
 * @User myGen
 * @Date 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_inaccount_order")
@ApiModel("tb_inaccount_order")
public class InaccountOrder extends Model<InaccountOrder> {
 
    @TableId(value = "order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "order_id")
    private String orderId;
 
    @TableField("order_type")
    @ApiModelProperty(value = "order_type")
    private String orderType;
 
    /**
     * 0：未审核，1：已审核
     */
    @TableField("check_stat")
    @ApiModelProperty(value = "0：未审核，1：已审核")
    private String checkStat;
 
    @TableField("remit_amt")
    @ApiModelProperty(value = "remit_amt")
    private BigDecimal remitAmt;
 
    @TableField("inacccount_amt")
    @ApiModelProperty(value = "inacccount_amt")
    private BigDecimal inacccountAmt;
 
    /**
     * 供应商ID
     */
    @TableField("provider_id")
    @ApiModelProperty(value = "供应商ID")
    private String providerId;
 
    /**
     * 企业ID
     */
    @TableField("company_id")
    @ApiModelProperty(value = "企业ID")
    private String companyId;
 
    /**
     * 0:未打款，1:已打款
     */
    @TableField("remit_check")
    @ApiModelProperty(value = "0:未打款，1:已打款")
    private String remitCheck;
 
    /**
     * 0:未上账，1:已上账
     */
    @TableField("inaccount_check")
    @ApiModelProperty(value = "0:未上账，1:已上账")
    private String inaccountCheck;
 
    /**
     * 0：未收款，1：已收款
     */
    @TableField("platform_receiver_check")
    @ApiModelProperty(value = "0：未收款，1：已收款")
    private String platformReceiverCheck;
 
    /**
     * 0：未收款，1：已收款
     */
    @TableField("company_receiver_check")
    @ApiModelProperty(value = "0：未收款，1：已收款")
    private String companyReceiverCheck;
 
    @TableField("evidence_url")
    @ApiModelProperty(value = "evidence_url")
    private String evidenceUrl;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
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
        return this.orderId;
    }
}
