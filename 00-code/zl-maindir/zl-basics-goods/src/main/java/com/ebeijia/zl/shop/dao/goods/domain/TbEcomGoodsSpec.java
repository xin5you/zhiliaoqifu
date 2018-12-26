package com.ebeijia.zl.shop.dao.goods.domain;

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
 * SKU规格对照表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_spec")
@ApiModel("SKU规格对照表")
public class TbEcomGoodsSpec extends Model<TbEcomGoodsSpec> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    /**
     * 商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    /**
     * SKU规格对照id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "SKU规格对照id")
    private String id;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    /**
     * 产品id
     */
    @TableField("product_id")
    @ApiModelProperty(value = "产品id")
    private String productId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * 规格id
     */
    @TableField("spec_id")
    @ApiModelProperty(value = "规格id")
    private String specId;
 
    /**
     * 规格值id
     */
    @TableField("spec_value_id")
    @ApiModelProperty(value = "规格值id")
    private String specValueId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
