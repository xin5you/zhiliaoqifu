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
 * 订单物流关联表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_express_platf")
@ApiModel("订单物流关联表")
public class TbEcomOrderExpressPlatf extends Model<TbEcomOrderExpressPlatf> {
 
    @TableField("o_item_id")
    @ApiModelProperty(value = "o_item_id")
    private String oItemId;
 
    @TableId(value = "o_pack_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "o_pack_id")
    private String oPackId;
 
    @TableField("pack_id")
    @ApiModelProperty(value = "pack_id")
    private String packId;
 
    @TableField("sale_count")
    @ApiModelProperty(value = "sale_count")
    private Integer saleCount;
 
    @TableField("sku_code")
    @ApiModelProperty(value = "sku_code")
    private String skuCode;


    @Override
    protected Serializable pkVal() { 
        return this.oPackId;
    }
}
