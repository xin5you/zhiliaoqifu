package com.ebeijia.zl.basics.coupon.domain;

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
 * 卡密池
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_card_keys_pool")
@ApiModel("卡密池")
public class TbCardKeysPool extends Model<TbCardKeysPool> {
 
    /**
     * 结束有效期
     */
    @TableField("active_end_date")
    @ApiModelProperty(value = "结束有效期")
    private Long activeEndDate;
 
    /**
     * 起始有效期
     */
    @TableField("active_start_date")
    @ApiModelProperty(value = "起始有效期")
    private Long activeStartDate;
 
    /**
     * 卡密类型id
     */
    @TableField("card_code")
    @ApiModelProperty(value = "卡密类型id")
    private String cardCode;
 
    /**
     * 卡密码
     */
    @TableField("card_key")
    @ApiModelProperty(value = "卡密码")
    private String cardKey;
 
    /**
     * 卡号
     */
    @TableField("card_number")
    @ApiModelProperty(value = "卡号")
    private String cardNumber;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;
 
    /**
     * 0：未核销
            1：已核销
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "0：未核销            1：已核销")
    private String dataStat;
 
    /**
     * 是否可用
            0：未激活
            1：可用
     */
    @TableField("enable")
    @ApiModelProperty(value = "是否可用            0：未激活            1：可用")
    private String enable;
 
    /**
     * 卡密id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "卡密id")
    private String id;
 
    /**
     * 密码生成规则id
     */
    @TableField("key_rule_id")
    @ApiModelProperty(value = "密码生成规则id")
    private String keyRuleId;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;
 
    /**
     * 修改时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "修改时间")
    private Long updateTime;
 
    /**
     * 修改人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "修改人")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
