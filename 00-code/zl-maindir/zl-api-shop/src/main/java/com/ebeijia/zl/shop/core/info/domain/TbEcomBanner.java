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
 * Banner信息
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_banner")
@ApiModel("Banner信息")
public class TbEcomBanner extends Model<TbEcomBanner> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    /**
     * 图片地址
     */
    @TableField("image_url")
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
 
    /**
     * 目标链接
     */
    @TableField("banner_url")
    @ApiModelProperty(value = "目标链接")
    private String bannerUrl;
 
    /**
     * 规格
     */
    @TableField("spec")
    @ApiModelProperty(value = "规格")
    private String spec;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
