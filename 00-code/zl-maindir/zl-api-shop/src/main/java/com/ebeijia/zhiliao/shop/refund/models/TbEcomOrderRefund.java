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
 * 退款表
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_refund")
@ApiModel("退款表")
public class TbEcomOrderRefund extends Model<TbEcomOrderRefund> {
 
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
 
    @TableField("refund_amt")
    @ApiModelProperty(value = "refund_amt")
    private Integer refundAmt;
 
    @TableId(value = "refund_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "refund_id")
    private String refundId;
 
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
 
    @TableField("returns_id")
    @ApiModelProperty(value = "returns_id")
    private String returnsId;
 
    @TableField("s_order_id")
    @ApiModelProperty(value = "s_order_id")
    private String sOrderId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.refundId;
    }
}
