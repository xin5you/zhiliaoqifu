package com.ebeijia.zl.shop.dao.member.domain;

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
 * @Date 2018-12-07
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_banner")
@ApiModel("Banner信息")
public class TbEcomBanner extends Model<TbEcomBanner> {
 
    /**
     * 提示文字，不能超过200字节
     */
    @TableField("banner_text")
    @ApiModelProperty(value = "提示文字，不能超过200字节")
    private String bannerText;
 
    /**
     * 目标链接
     */
    @TableField("banner_url")
    @ApiModelProperty(value = "目标链接")
    private String bannerUrl;
 
    /**
     * 0表示正常，1表示已经删除
     */
    @TableField("disable")
    @ApiModelProperty(value = "0表示正常，1表示已经删除")
    private String disable;
 
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
     * Banner位置
     */
    @TableField("position")
    @ApiModelProperty(value = "Banner位置")
    private String position;
 
    /**
     * 数字越小越靠前
     */
    @TableField("sort")
    @ApiModelProperty(value = "数字越小越靠前")
    private Integer sort;
 
    /**
     * 规格，代表类型
     */
    @TableField("spec")
    @ApiModelProperty(value = "规格，代表类型")
    private String spec;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
