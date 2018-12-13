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
 * 记录用户持有的代金券商品
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_card_keys")
@ApiModel("记录用户持有的代金券商品")
public class TbCardKeys extends Model<TbCardKeys> {
 
    /**
     * 用户ID
     */
    @TableField("account_id")
    @ApiModelProperty(value = "用户ID")
    private String accountId;
 
    /**
     * 结束有效期
     */
    @TableField("active_end_date")
    @ApiModelProperty(value = "结束有效期")
    private String activeEndDate;
 
    /**
     * 起始有效期
     */
    @TableField("active_start_date")
    @ApiModelProperty(value = "起始有效期")
    private String activeStartDate;
 
    /**
     * 代金券id
     */
    @TableId(value = "coupon_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "代金券id")
    private String couponId;
 
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
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 代金券商品id
     */
    @TableField("product_code")
    @ApiModelProperty(value = "代金券商品id")
    private String productCode;
 
    /**
     * 供应商ID
     */
    @TableField("provider_id")
    @ApiModelProperty(value = "供应商ID")
    private String providerId;
 
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
        return this.couponId;
    }
}
