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
 * 渠道设置
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_setting")
@ApiModel("渠道设置")
public class TbEcomSetting extends Model<TbEcomSetting> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("ecom_name")
    @ApiModelProperty(value = "ecom_name")
    private String ecomName;
 
    @TableField("ecom_code")
    @ApiModelProperty(value = "ecom_code")
    private String ecomCode;
 
    /**
     * 1000:第三方商城
                        2000：自建商城
     */
    @TableField("ecom_type")
    @ApiModelProperty(value = "1000:第三方商城                        2000：自建商城")
    private String ecomType;
 
    /**
     * 单位：分
     */
    @TableField("full_money")
    @ApiModelProperty(value = "单位：分")
    private Integer fullMoney;
 
    /**
     * 单位：分
     */
    @TableField("ecom_freight")
    @ApiModelProperty(value = "单位：分")
    private Integer ecomFreight;
 
    @TableField("shop_desc")
    @ApiModelProperty(value = "shop_desc")
    private String shopDesc;
 
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
        return this.id;
    }
}
