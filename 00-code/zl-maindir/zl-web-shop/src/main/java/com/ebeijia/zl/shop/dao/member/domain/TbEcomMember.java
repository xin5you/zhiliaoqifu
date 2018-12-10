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
 * 会员信息表
 *
 * @User J
 * @Date 2018-12-06
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_member")
@ApiModel("会员信息表")
public class TbEcomMember extends Model<TbEcomMember> {
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;
 
    @TableField("lv_id")
    @ApiModelProperty(value = "lv_id")
    private String lvId;
 
    @TableId(value = "member_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "member_id")
    private String memberId;
 
    @TableField("open_id")
    @ApiModelProperty(value = "open_id")
    private String openId;
 
    @TableField("person_id")
    @ApiModelProperty(value = "person_id")
    private String personId;
 
    @TableField("point")
    @ApiModelProperty(value = "point")
    private Integer point;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("user_id")
    @ApiModelProperty(value = "user_id")
    private String userId;


    @Override
    protected Serializable pkVal() { 
        return this.memberId;
    }
}
