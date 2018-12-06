package com.ebeijia.zl.shop.core.goods.domain;

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
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_spec")
@ApiModel("SKU规格对照表")
public class TbEcomGoodsSpec extends Model<TbEcomGoodsSpec> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("spec_id")
    @ApiModelProperty(value = "spec_id")
    private String specId;
 
    @TableField("spec_value_id")
    @ApiModelProperty(value = "spec_value_id")
    private String specValueId;
 
    @TableField("goods_id")
    @ApiModelProperty(value = "goods_id")
    private String goodsId;
 
    @TableField("product_id")
    @ApiModelProperty(value = "product_id")
    private String productId;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
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
        return this.id;
    }
}
