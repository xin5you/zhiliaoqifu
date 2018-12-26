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
 * 渠道交易流水明细
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_dms_related_detail")
@ApiModel("渠道交易流水明细")
public class TbEcomDmsRelatedDetail extends Model<TbEcomDmsRelatedDetail> {
 
    /**
     * 专项账户ID 如果是通卡账户，取默认值0
     */
    @TableField("b_id")
    @ApiModelProperty(value = "专项账户ID 如果是通卡账户，取默认值0")
    private String bId;
 
    /**
     * 专项账户名
     */
    @TableField("b_name")
    @ApiModelProperty(value = "专项账户名")
    private String bName;
 
    /**
     * 扣款金额 单位：分
     */
    @TableField("debit_price")
    @ApiModelProperty(value = "扣款金额 单位：分")
    private Integer debitPrice;
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    /**
     * 关联支付明细
     */
    @TableField("pay_details_id")
    @ApiModelProperty(value = "关联支付明细")
    private String payDetailsId;
 
    /**
     * 渠道子订单ID
     */
    @TableField("s_order_id")
    @ApiModelProperty(value = "渠道子订单ID")
    private String sOrderId;
 
    @TableField("备用1")
    @ApiModelProperty(value = "备用1")
    private String 备用1;
 
    @TableField("备用2")
    @ApiModelProperty(value = "备用2")
    private String 备用2;
 
    @TableField("备用3")
    @ApiModelProperty(value = "备用3")
    private String 备用3;
 
    @TableField("备用4")
    @ApiModelProperty(value = "备用4")
    private String 备用4;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
