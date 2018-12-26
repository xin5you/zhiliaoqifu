package com.ebeijia.zl.shop.dao.order.domain;

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
 * 订单收货地址
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_order_ship")
@ApiModel("订单收货地址")
public class TbEcomOrderShip extends Model<TbEcomOrderShip> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("order_id")
    @ApiModelProperty(value = "order_id")
    private String orderId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("ship_addr")
    @ApiModelProperty(value = "ship_addr")
    private String shipAddr;
 
    @TableField("ship_city_id")
    @ApiModelProperty(value = "ship_city_id")
    private String shipCityId;
 
    @TableField("ship_email")
    @ApiModelProperty(value = "ship_email")
    private String shipEmail;
 
    @TableField("ship_mobile")
    @ApiModelProperty(value = "ship_mobile")
    private String shipMobile;
 
    @TableField("ship_name")
    @ApiModelProperty(value = "ship_name")
    private String shipName;
 
    @TableField("ship_province_id")
    @ApiModelProperty(value = "ship_province_id")
    private String shipProvinceId;
 
    @TableField("ship_region_id")
    @ApiModelProperty(value = "ship_region_id")
    private String shipRegionId;
 
    @TableField("ship_telephone")
    @ApiModelProperty(value = "ship_telephone")
    private String shipTelephone;
 
    @TableField("ship_zip_code")
    @ApiModelProperty(value = "ship_zip_code")
    private String shipZipCode;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
