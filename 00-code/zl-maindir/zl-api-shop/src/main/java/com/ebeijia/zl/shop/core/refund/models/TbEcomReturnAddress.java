package com.ebeijia.zl.shop.core.refund.models;

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
 * 退货收货地址
 *
 * @User J
 * @Date 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_return_address")
@ApiModel("退货收货地址")
public class TbEcomReturnAddress extends Model<TbEcomReturnAddress> {
 
    @TableField("address")
    @ApiModelProperty(value = "address")
    private String address;
 
    @TableField("city_name")
    @ApiModelProperty(value = "city_name")
    private String cityName;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("district_name")
    @ApiModelProperty(value = "district_name")
    private String districtName;
 
    @TableField("full_address")
    @ApiModelProperty(value = "full_address")
    private String fullAddress;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("mobile")
    @ApiModelProperty(value = "mobile")
    private String mobile;
 
    @TableField("name")
    @ApiModelProperty(value = "name")
    private String name;
 
    @TableField("province_name")
    @ApiModelProperty(value = "province_name")
    private String provinceName;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableId(value = "ret_addr_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "ret_addr_id")
    private String retAddrId;
 
    @TableField("returns_desc")
    @ApiModelProperty(value = "returns_desc")
    private String returnsDesc;
 
    @TableField("returns_id")
    @ApiModelProperty(value = "returns_id")
    private String returnsId;
 
    @TableField("telephone")
    @ApiModelProperty(value = "telephone")
    private String telephone;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("zip_code")
    @ApiModelProperty(value = "zip_code")
    private String zipCode;


    @Override
    protected Serializable pkVal() { 
        return this.retAddrId;
    }
}
