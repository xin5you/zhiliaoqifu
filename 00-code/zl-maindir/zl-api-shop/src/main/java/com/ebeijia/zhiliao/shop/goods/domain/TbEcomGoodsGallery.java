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
 * 商品相册表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_goods_gallery")
@ApiModel("商品相册表")
public class TbEcomGoodsGallery extends Model<TbEcomGoodsGallery> {
 
    /**
     * 图片id
     */
    @TableId(value = "img_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "图片id")
    private String imgId;
 
    /**
     * 商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
 
    /**
     * 用于各商品列表中
     */
    @TableField("thumbnail")
    @ApiModelProperty(value = "用于各商品列表中")
    private String thumbnail;
 
    /**
     * 用于商品详细中preview
     */
    @TableField("small")
    @ApiModelProperty(value = "用于商品详细中preview")
    private String small;
 
    @TableField("big")
    @ApiModelProperty(value = "big")
    private String big;
 
    @TableField("original")
    @ApiModelProperty(value = "original")
    private String original;
 
    /**
     * 用于商品详细页中主图下的小列表图
     */
    @TableField("tiny")
    @ApiModelProperty(value = "用于商品详细页中主图下的小列表图")
    private String tiny;
 
    /**
     * 0：是，1：否
     */
    @TableField("is_default")
    @ApiModelProperty(value = "0：是，1：否")
    private String isDefault;
 
    /**
     * 图片排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "图片排序")
    private Integer sort;
 
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
        return this.imgId;
    }
}
