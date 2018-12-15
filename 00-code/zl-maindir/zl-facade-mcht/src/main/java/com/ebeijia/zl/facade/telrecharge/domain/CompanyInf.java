package com.ebeijia.zl.facade.telrecharge.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 需要开通专用账户员工的所属企业
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_company_inf")
public class CompanyInf extends Model<CompanyInf> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 4797212919449958495L;

	/**
     * 企业id
     */
    @TableId(value = "company_id" ,type = IdType.UUID)
    private String companyId;
 
    /**
     * 统一社会信用代码
     */
    @TableField("law_code")
    private String lawCode;
 
    /**
     * 0：允许交易 1：不允许交易
     */
    @TableField("trans_flag")
    private String transFlag;
 
    /**
     * 名称
     */
    @TableField("name")
    private String name;
 
    /**
     * 地址
     */
    @TableField("address")
    private String address;
 
    /**
     * 联系电话
     */
    @TableField("phone_no")
    private String phoneNo;
 
    /**
     * 联系人
     */
    @TableField("contacts")
    private String contacts;
 
    /**
     * 0:否，1：是
     */
    @TableField("is_open")
    private String isOpen;
 
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
        return this.companyId;
    }
}
