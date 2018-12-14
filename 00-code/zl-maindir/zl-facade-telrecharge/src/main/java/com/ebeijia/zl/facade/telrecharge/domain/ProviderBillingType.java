package com.ebeijia.zl.facade.telrecharge.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 供应商账户类型信息
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_provider_billingtype")
public class ProviderBillingType extends Model<ProviderBillingType> {
	
	/**
     * id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;
 
    /**
     * provider_id
     */
    @TableId(value = "provider_id")
    private String providerId;
 
    /**
     * 专项类型id
     */
    @TableField("b_id")
    private String bId;
 
    @Override
    protected Serializable pkVal() { 
        return this.providerId;
    }

}
