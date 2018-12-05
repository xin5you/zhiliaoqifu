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
 * 商品规格表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_specification")
@ApiModel("商品规格表")
public class TbEcomSpecification extends Model<TbEcomSpecification> {
 
    /**
     * 规格id
     */
    @TableId(value = "spec_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "规格id")
    private String specId;
 
    /**
     * 规格名
     */
    @TableField("spec_name")
    @ApiModelProperty(value = "规格名")
    private String specName;
 
    /**
     * 规格图片
     */
    @TableField("spec_img")
    @ApiModelProperty(value = "规格图片")
    private String specImg;
 
    /**
     * 规格排序
     */
    @TableField("spec_order")
    @ApiModelProperty(value = "规格排序")
    private Integer specOrder;
 
    /**
     * 0：否，1：是
     */
    @TableField("is_del")
    @ApiModelProperty(value = "0：否，1：是")
    private String isDel;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
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
        return this.specId;
    }
}
