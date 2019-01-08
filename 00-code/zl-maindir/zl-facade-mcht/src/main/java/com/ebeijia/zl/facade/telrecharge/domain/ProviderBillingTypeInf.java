package com.ebeijia.zl.facade.telrecharge.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 供应商专项类型关联信息
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_provider_billingtype_inf")
public class ProviderBillingTypeInf extends Model<ProviderBillingTypeInf> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 4797212919449958495L;

    /**
     * 主键id
     */
    @TableId(value = "id" ,type = IdType.UUID)
    private String id;

    /**
     * 供应商id
     */
    @TableId(value = "provider_id")
    private String providerId;

	/**
     * 专项类型id
     */
    @TableId(value = "b_id")
    private String bId;

    /**
     * 费率
     */
    @TableId(value = "fee")
    private String fee;
 
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
    @Version
    @TableField("lock_version")
    private Integer lockVersion;

    //供应商名称
    @TableField(exist = false)
    private String providerName;

    //专项类型名称
    @TableField(exist = false)
    private String bName;

    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
