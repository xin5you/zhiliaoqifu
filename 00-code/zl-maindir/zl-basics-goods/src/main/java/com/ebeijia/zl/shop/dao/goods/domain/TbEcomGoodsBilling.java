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
 * 商品专项账户关联表
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_billing")
@ApiModel("商品专项账户关联表")
public class TbEcomGoodsBilling extends Model<TbEcomGoodsBilling> {
 
    /**
     * BID标记
     */
    @TableField("b_id")
    @ApiModelProperty(value = "BID标记")
    private String bId;
 
    /**
     * 商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    @TableId(value = "id" ,type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
 
    @TableField("备用字段")
    @ApiModelProperty(value = "备用字段")
    private String 备用字段;
 
    @TableField("备用字段2")
    @ApiModelProperty(value = "备用字段2")
    private String 备用字段2;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
