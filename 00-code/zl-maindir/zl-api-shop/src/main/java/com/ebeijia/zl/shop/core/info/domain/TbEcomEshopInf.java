package com.ebeijia.zl.shop.core.info.domain;

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
 * eshop信息表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_eshop_inf")
@ApiModel("eshop信息表")
public class TbEcomEshopInf extends Model<TbEcomEshopInf> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("mchnt_code")
    @ApiModelProperty(value = "mchnt_code")
    private String mchntCode;
 
    @TableField("shop_code")
    @ApiModelProperty(value = "shop_code")
    private String shopCode;
 
    @TableField("eshop_name")
    @ApiModelProperty(value = "eshop_name")
    private String eshopName;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
