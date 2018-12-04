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
 * eshop路由关联表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_eshop_routes_ref")
@ApiModel("eshop路由关联表")
public class TbEcomEshopRoutesRef extends Model<TbEcomEshopRoutesRef> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("tb__id")
    @ApiModelProperty(value = "tb__id")
    private String tb_id;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
