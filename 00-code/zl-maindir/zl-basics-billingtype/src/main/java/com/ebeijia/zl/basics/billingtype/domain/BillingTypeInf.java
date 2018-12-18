package com.ebeijia.zl.basics.billingtype.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_billing_type")
public class BillingTypeInf extends Model<BaseDict> {
	/**
     * id
     */
    @TableId(value = "b_id" ,type = IdType.UUID)
	private String bId;
    
    /**
     * 名称
     */
    @TableField("b_name")
	private String bName;
    
    /**
     * 代码
     */
    @TableField("code")
	private String code;
    
    /**
     * 折损率
     */
    @TableField("lose_fee")
	private BigDecimal loseFee;
    
    /**
     * 可够率
     */
    @TableField("buy_fee")
	private BigDecimal buyFee;
    
    /**
     * 状态
     */
    @TableField("data_stat")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.bId;
    }
}
