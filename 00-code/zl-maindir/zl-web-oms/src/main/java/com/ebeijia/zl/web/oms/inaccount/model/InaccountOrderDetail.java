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
 * tb_inaccount_order_detail
 *
 * @User myGen
 * @Date 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_inaccount_order_detail")
@ApiModel("tb_inaccount_order_detail")
public class InaccountOrderDetail extends Model<InaccountOrderDetail> {
 
    @TableId(value = "order_list_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "order_list_id")
    private String orderListId;
 
    @TableField("order_id")
    @ApiModelProperty(value = "order_id")
    private String orderId;
 
    /**
     * 0：未开票，1：已开票
     */
    @TableField("is_invoice")
    @ApiModelProperty(value = "0：未开票，1：已开票")
    private String isInvoice;
 
    @TableField("trans_amt")
    @ApiModelProperty(value = "trans_amt")
    private BigDecimal transAmt;
 
    @TableField("b_id")
    @ApiModelProperty(value = "b_id")
    private String bId;
 
    @TableField("invoice_info")
    @ApiModelProperty(value = "invoice_info")
    private String invoiceInfo;
 
    @TableField("platform_in_amt")
    @ApiModelProperty(value = "platform_in_amt")
    private BigDecimal platformInAmt;
 
    @TableField("company_in_amt")
    @ApiModelProperty(value = "company_in_amt")
    private BigDecimal companyInAmt;

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
        return this.orderListId;
    }
}
