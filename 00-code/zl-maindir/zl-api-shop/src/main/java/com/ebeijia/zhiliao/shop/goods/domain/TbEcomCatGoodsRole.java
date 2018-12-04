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
 * 商品分类关联表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_cat_goods_role")
@ApiModel("商品分类关联表")
public class TbEcomCatGoodsRole extends Model<TbEcomCatGoodsRole> {
 
    /**
     * 分类ID
     */
    @TableId(value = "cat_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "分类ID")
    private String catId;
 
    /**
     * 商品ID
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
 
    /**
     * 状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "状态")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
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
        return this.catId;
    }
}
