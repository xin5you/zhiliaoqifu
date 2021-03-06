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
 * 商品规格明细
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_spec_values")
@ApiModel("商品规格明细")
public class TbEcomSpecValues extends Model<TbEcomSpecValues> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("inherent_or_add")
    @ApiModelProperty(value = "inherent_or_add")
    private Integer inherentOrAdd;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    /**
     * 对应规格id
     */
    @TableField("spec_id")
    @ApiModelProperty(value = "对应规格id")
    private String specId;
 
    /**
     * 规格值图片
     */
    @TableField("spec_image")
    @ApiModelProperty(value = "规格值图片")
    private String specImage;
 
    /**
     * 排序号
     */
    @TableField("spec_order")
    @ApiModelProperty(value = "排序号")
    private Integer specOrder;
 
    /**
     * 0：文字，1：图片
     */
    @TableField("spec_type")
    @ApiModelProperty(value = "0：文字，1：图片")
    private String specType;
 
    /**
     * 规格值
     */
    @TableField("spec_value")
    @ApiModelProperty(value = "规格值")
    private String specValue;
 
    /**
     * 规格值id
     */
    @TableId(value = "spec_value_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "规格值id")
    private String specValueId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;

    @TableField("spec_value_name")
    @ApiModelProperty(value = "规格值名称")
    private String specValueName;

    @TableField(exist = false)
    private String specName;

    @Override
    protected Serializable pkVal() { 
        return this.specValueId;
    }
}
