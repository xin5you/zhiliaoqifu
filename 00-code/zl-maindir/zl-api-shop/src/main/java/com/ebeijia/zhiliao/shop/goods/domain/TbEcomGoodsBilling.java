package com.ebeijia.zhiliao.shop.goods.domain;

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
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_billing")
@ApiModel("商品专项账户关联表")
public class TbEcomGoodsBilling extends Model<TbEcomGoodsBilling> {
 
    /**
     * 商品id
     */
    @TableId(value = "goods_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    /**
     * 如果是仅能使用通卡购买的情况，记为默认值0
     */
    @TableField("b_id")
    @ApiModelProperty(value = "如果是仅能使用通卡购买的情况，记为默认值0")
    private String bId;
 
    /**
     * 账户名
     */
    @TableField("b_name")
    @ApiModelProperty(value = "账户名")
    private String bName;
 
    @TableField("备用字段")
    @ApiModelProperty(value = "备用字段")
    private String 备用字段;
 
    @TableField("备用字段2")
    @ApiModelProperty(value = "备用字段2")
    private String 备用字段2;


    @Override
    protected Serializable pkVal() { 
        return this.goodsId;
    }
}
