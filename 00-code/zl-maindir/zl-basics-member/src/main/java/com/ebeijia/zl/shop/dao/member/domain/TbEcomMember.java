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
 * @User zl_shop
 * @Date 2018-12-26
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
 
    /**
     * 主键id
     */
    @TableId(value = "member_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
    private String memberId;
 
    /**
     * 记录的open_id
     */
    @TableField("open_id")
    @ApiModelProperty(value = "记录的open_id")
    private String openId;
 
    /**
     * 会员id
     */
    @TableField("person_id")
    @ApiModelProperty(value = "会员id")
    private String personId;
 
    /**
     * 会员
     */
    @TableField("phone")
    @ApiModelProperty(value = "会员")
    private String phone;
 
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
 
    /**
     * 关联用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "关联用户id")
    private String userId;


    @Override
    protected Serializable pkVal() { 
        return this.memberId;
    }
}
