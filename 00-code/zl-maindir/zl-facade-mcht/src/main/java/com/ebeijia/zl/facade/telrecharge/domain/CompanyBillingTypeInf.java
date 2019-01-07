package com.ebeijia.zl.facade.telrecharge.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 企业专项类型关联信息
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_company_billingtype_inf")
public class CompanyBillingTypeInf extends Model<CompanyBillingTypeInf> {
 
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
     * 企业id
     */
    @TableId(value = "company_id")
    private String companyId;

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

    //企业名称
    @TableField(exist = false)
    private String companyName;

    //专项类型名称
    @TableField(exist = false)
    private String bName;

    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
