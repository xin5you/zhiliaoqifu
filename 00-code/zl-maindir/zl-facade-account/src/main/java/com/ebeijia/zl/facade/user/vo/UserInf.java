package com.cn.thinkx.ebj.facade.user.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * 用户信息
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_user_inf")
public class UserInf extends Model<UserInf> {
 
    /**
     * 用户信息_id
     */
    @TableId(value = "user_id" ,type = IdType.UUID)
    private String userId;
 
    /**
     * 企业id
     */
    @TableField("company_id")
    private String companyId;
 
    /**
     * 关键业务数据
     */
    @TableField("user_name")
    private String userName;
 
    /**
     * 100：企业员工账户
            200：企业账户
            300：供应商账户
            400：分销商账户
     */
    @TableField("user_type")
    private String userType;
 
    /**
     * 密码
     */
    @TableField("user_passwd")
    private String userPasswd;
 
    /**
     * 产品号
     */
    @TableField("product_code")
    private String productCode;
 
    /**
     * 数据状态
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
        return this.userId;
    }
}
