package com.ebeijia.zl.shop.vo;


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
 * 企业员工在平台账户类型
 *
 * @Date 2018-12-18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_billing_type")
@ApiModel("企业员工在平台账户类型")
public class BillingType extends Model<BillingType> {

    /**
     * 专项类型id
     */
    @TableId(value = "b_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "专项类型id")
    private String bId;

    /**
     * 专项名称
     */
    @TableField("b_name")
    @ApiModelProperty(value = "专项名称")
    private String bName;

    /**
     * A类账户
     B类账户
     C类账户
     */
    @TableField("code")
    @ApiModelProperty(value = "A类账户            B类账户            C类账户")
    private String code;

    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;

    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 折损率
     */
    @TableField("lose_fee")
    @ApiModelProperty(value = "折损率")
    private BigDecimal loseFee;

    /**
     * 可购率
     */
    @TableField("buy_fee")
    @ApiModelProperty(value = "可购率")
    private BigDecimal buyFee;


    @Override
    protected Serializable pkVal() {
        return this.bId;
    }
}

