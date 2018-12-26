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
 * 会员收货地址
 *
 * @User zl_shop
 * @Date 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_member_address")
@ApiModel("会员收货地址")
public class TbEcomMemberAddress extends Model<TbEcomMemberAddress> {
 
    @TableField("add_zip")
    @ApiModelProperty(value = "add_zip")
    private String addZip;
 
    @TableField("addr_detail")
    @ApiModelProperty(value = "addr_detail")
    private String addrDetail;
 
    @TableId(value = "addr_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "addr_id")
    private String addrId;
 
    @TableField("city")
    @ApiModelProperty(value = "city")
    private String city;
 
    @TableField("city_id")
    @ApiModelProperty(value = "city_id")
    private String cityId;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("def_addr")
    @ApiModelProperty(value = "def_addr")
    private String defAddr;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("member_id")
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("mobile")
    @ApiModelProperty(value = "mobile")
    private String mobile;
 
    @TableField("province")
    @ApiModelProperty(value = "province")
    private String province;
 
    @TableField("province_id")
    @ApiModelProperty(value = "province_id")
    private String provinceId;
 
    @TableField("region")
    @ApiModelProperty(value = "region")
    private String region;
 
    @TableField("region_id")
    @ApiModelProperty(value = "region_id")
    private String regionId;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("ship_address_name")
    @ApiModelProperty(value = "ship_address_name")
    private String shipAddressName;
 
    @TableField("tel")
    @ApiModelProperty(value = "tel")
    private String tel;
 
    @TableField("town")
    @ApiModelProperty(value = "town")
    private String town;
 
    @TableField("town_id")
    @ApiModelProperty(value = "town_id")
    private String townId;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("user_name")
    @ApiModelProperty(value = "user_name")
    private String userName;


    @Override
    protected Serializable pkVal() { 
        return this.addrId;
    }
}
