package com.ebeijia.zhiliao.shop.info.domain;

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
 * eshop_banner关联
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_eshop_banner_ref")
@ApiModel("eshop_banner关联")
public class TbEcomEshopBannerRef extends Model<TbEcomEshopBannerRef> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("banner_id")
    @ApiModelProperty(value = "banner_id")
    private String bannerId;
 
    @TableField("eshop_id")
    @ApiModelProperty(value = "eshop_id")
    private String eshopId;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
