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
 * 渠道支付交易表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_pay_order")
@ApiModel("渠道支付交易表")
public class TbEcomPayOrder extends Model<TbEcomPayOrder> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("dms_related_key")
    @ApiModelProperty(value = "dms_related_key")
    private String dmsRelatedKey;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("member_id")
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("org_dms_related_key")
    @ApiModelProperty(value = "org_dms_related_key")
    private String orgDmsRelatedKey;
 
    @TableField("org_pay_order_id")
    @ApiModelProperty(value = "org_pay_order_id")
    private String orgPayOrderId;
 
    @TableField("out_trans_no")
    @ApiModelProperty(value = "out_trans_no")
    private String outTransNo;
 
    @TableId(value = "pay_order_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "pay_order_id")
    private String payOrderId;
 
    @TableField("post_type")
    @ApiModelProperty(value = "post_type")
    private String postType;
 
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
 
    @TableField("trans_shop_name")
    @ApiModelProperty(value = "trans_shop_name")
    private String transShopName;
 
    @TableField("trans_shop_nunber")
    @ApiModelProperty(value = "trans_shop_nunber")
    private String transShopNunber;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.payOrderId;
    }
}
