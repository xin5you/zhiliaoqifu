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
 * 入口路由表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_routes")
@ApiModel("入口路由表")
public class TbEcomRoutes extends Model<TbEcomRoutes> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("ecom_name")
    @ApiModelProperty(value = "ecom_name")
    private String ecomName;
 
    @TableField("ecom_code")
    @ApiModelProperty(value = "ecom_code")
    private String ecomCode;
 
    @TableField("ecom_url")
    @ApiModelProperty(value = "ecom_url")
    private String ecomUrl;
 
    @TableField("ecom_type")
    @ApiModelProperty(value = "ecom_type")
    private String ecomType;
 
    @TableField("ecom_logo")
    @ApiModelProperty(value = "ecom_logo")
    private String ecomLogo;
 
    @TableField("order_url")
    @ApiModelProperty(value = "order_url")
    private String orderUrl;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
